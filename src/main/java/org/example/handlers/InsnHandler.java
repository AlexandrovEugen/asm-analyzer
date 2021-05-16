package org.example.handlers;

import lombok.SneakyThrows;
import org.example.models.Instruction;
import org.example.models.InvokedMethod;
import org.example.models.NewInstance;
import org.example.models.Param;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.*;
import java.util.stream.Collectors;

public class InsnHandler {

    private final InsnList instructions;

    public InsnHandler(InsnList instructions) {
        this.instructions = instructions;
    }


    public List<Instruction> getInstructions() {
        List<List<AbstractInsnNode>> nodesList = splitByLines(instructions);
        return nodesList.stream()
                .map(this::nodesToInstruction)
                .filter(i -> Objects.nonNull(i.getNewInstance()) || Objects.nonNull(i.getInvokedMethod()))
                .collect(Collectors.toList());
    }

    private Instruction nodesToInstruction(List<AbstractInsnNode> insnNodes) {
        final Instruction instruction = new Instruction();
        final Stack<TypeInsnNode> instances = new Stack<>();
        final Queue<LdcInsnNode> constants = new LinkedList<>();
        insnNodes.forEach(ins -> handleInsnNode(ins, instruction, instances, constants));
        return instruction;
    }

    private List<List<AbstractInsnNode>> splitByLines(InsnList instructions) {
        final List<List<AbstractInsnNode>> insNodesList = new ArrayList<>();
        List<AbstractInsnNode> insNodes = new ArrayList<>();
        for (final AbstractInsnNode current : instructions.toArray()) {
            if (current.getType() != AbstractInsnNode.LABEL) {
                insNodes.add(current);
                if (current.getNext().getType() == AbstractInsnNode.LABEL) {
                    insNodesList.add(insNodes);
                    insNodes = new ArrayList<>();
                }
            }
        }
        return insNodesList;
    }


    private void handleInsnNode(AbstractInsnNode insNode, Instruction methodIns, Stack<TypeInsnNode> instances, Queue<LdcInsnNode> constants) {
        switch (insNode.getType()) {
            case AbstractInsnNode.FIELD_INSN:
                FieldInsnNode fieldInsnNode = (FieldInsnNode) insNode;
                break;
            case AbstractInsnNode.INSN:
                InsnNode insnNode = (InsnNode) insNode;
                break;
            case AbstractInsnNode.IINC_INSN:
                IincInsnNode iincInsnNode = (IincInsnNode) insNode;
                break;
            case AbstractInsnNode.INT_INSN:
                IntInsnNode intInsnNode = (IntInsnNode) insNode;
                break;
            case AbstractInsnNode.JUMP_INSN:
                JumpInsnNode jumpInsnNode = (JumpInsnNode) insNode;
                break;
            case AbstractInsnNode.LDC_INSN:
                LdcInsnNode ldcInsnNode = (LdcInsnNode) insNode;
                constants.add(ldcInsnNode);
                break;
            case AbstractInsnNode.LINE:
                LineNumberNode lineNumberNode = (LineNumberNode) insNode;
                methodIns.setLine(lineNumberNode.line);
                break;
            case AbstractInsnNode.LOOKUPSWITCH_INSN:
                LookupSwitchInsnNode lookupSwitchInsnNode = (LookupSwitchInsnNode) insNode;
                break;
            case AbstractInsnNode.METHOD_INSN:
                handleMethodInsNode(methodIns, (MethodInsnNode) insNode, instances, constants);
                break;
            case AbstractInsnNode.TABLESWITCH_INSN:
                TableSwitchInsnNode tableSwitchInsnNode = (TableSwitchInsnNode) insNode;
                break;
            case AbstractInsnNode.TYPE_INSN:
                handleTypeInsNode((TypeInsnNode) insNode, instances);
                break;
            case AbstractInsnNode.VAR_INSN:
                VarInsnNode varInsnNode = (VarInsnNode) insNode;
                break;
        }
    }

    @SneakyThrows
    private void handleTypeInsNode(TypeInsnNode typeInsnNode, Stack<TypeInsnNode> instances) {
        if (typeInsnNode.getOpcode() == Opcodes.NEW) {
            instances.push(typeInsnNode);
        }
    }

    private void handleMethodInsNode(Instruction methodIns,
                                     MethodInsnNode methodInsnNode,
                                     Stack<TypeInsnNode> instances,
                                     Queue<LdcInsnNode> constants) {
        if (methodInsnNode.getOpcode() == Opcodes.INVOKESPECIAL) {
            if (!instances.isEmpty()) {
                final TypeInsnNode lastAddedInstance = instances.pop();
                final NewInstance newInstance = new NewInstance();
                newInstance.setType(lastAddedInstance.desc);
                newInstance.setParams(paramsFromDescriptor(methodInsnNode.desc, constants));
                methodIns.setNewInstance(newInstance);
            }
        } else {
            final InvokedMethod invokedMethod = new InvokedMethod();
            invokedMethod.setOwner(methodInsnNode.owner);
            invokedMethod.setName(methodInsnNode.name);
            invokedMethod.setParams(paramsFromDescriptor(methodInsnNode.desc, constants));
            methodIns.setInvokedMethod(invokedMethod);
        }
    }

    private List<Param> paramsFromDescriptor(String desc, Queue<LdcInsnNode> constants) {
        return Arrays.stream(Type.getArgumentTypes(desc))
                .map(Type::getClassName)
                .map(type -> {
                    final LdcInsnNode valueNode = constants.poll();
                    final Param param = new Param();
                    param.setType(type);
                    if (Objects.nonNull(valueNode)) {
                        param.setValue(valueNode.cst);
                    }
                    return param;
                })
                .collect(Collectors.toList());
    }

}
