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
                .collect(Collectors.toList());
    }

    private Instruction nodesToInstruction(List<AbstractInsnNode> insnNodes) {
        Instruction instruction = new Instruction();
        insnNodes.forEach(ins -> handleInsnNode(ins, instruction));
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


    private void handleInsnNode(AbstractInsnNode insNode, Instruction methodIns) {
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
            case AbstractInsnNode.FRAME:
                FrameNode frameNode = (FrameNode) insNode;
                break;
            case AbstractInsnNode.INVOKE_DYNAMIC_INSN:
                InvokeDynamicInsnNode invokeDynamicInsnNode = (InvokeDynamicInsnNode) insNode;
                break;
            case AbstractInsnNode.LABEL:
                LabelNode labelNode = (LabelNode) insNode;
                break;
            case AbstractInsnNode.LDC_INSN:
                LdcInsnNode ldcInsnNode = (LdcInsnNode) insNode;
                break;
            case AbstractInsnNode.LINE:
                LineNumberNode lineNumberNode = (LineNumberNode) insNode;
                methodIns.setLine(lineNumberNode.line);
                break;
            case AbstractInsnNode.LOOKUPSWITCH_INSN:
                LookupSwitchInsnNode lookupSwitchInsnNode = (LookupSwitchInsnNode) insNode;
                break;
            case AbstractInsnNode.METHOD_INSN:
                handleMethodInsNode(methodIns, (MethodInsnNode) insNode);
                break;
            case AbstractInsnNode.TABLESWITCH_INSN:
                TableSwitchInsnNode tableSwitchInsnNode = (TableSwitchInsnNode) insNode;
                break;
            case AbstractInsnNode.TYPE_INSN:
                handleTypeInsNode((TypeInsnNode) insNode, methodIns);
                break;
            case AbstractInsnNode.VAR_INSN:
                VarInsnNode varInsnNode = (VarInsnNode) insNode;
                break;
        }
    }

    @SneakyThrows
    private void handleTypeInsNode(TypeInsnNode typeInsnNode, Instruction methodIns) {
        if (typeInsnNode.getOpcode() == Opcodes.NEW) {
            final NewInstance instanceWithConstructor = methodIns.getNewInstance();
            if (Objects.nonNull(instanceWithConstructor)) {
                final Param param = new Param();
                param.setType(typeInsnNode.desc);
                instanceWithConstructor.addParam(param);
            } else {
                final NewInstance newInstance = new NewInstance();
                newInstance.setType(typeInsnNode.desc);
                methodIns.setNewInstance(newInstance);
            }
        }
    }

    private void handleMethodInsNode(Instruction methodIns, MethodInsnNode methodInsnNode) {
        if (methodInsnNode.getOpcode() != Opcodes.INVOKESPECIAL) {
            final NewInstance mainInstance = methodIns.getNewInstance();
            if (Objects.nonNull(mainInstance)) {
                final Param param = new Param();
                param.setType(Type.getReturnType(methodInsnNode.desc).getClassName());
                mainInstance.addParam(param);
            } else {
                final InvokedMethod invokedMethod = new InvokedMethod();
                invokedMethod.setName(methodInsnNode.name);
                invokedMethod.setOwner(methodInsnNode.owner);
                methodIns.setInvokedMethod(invokedMethod);
            }
        }
    }

    private class InstructionMeta {
        private final Map<String, TypeInsnNode> newInstanceMap = new HashMap<>();
        private final Map<String, MethodInsnNode> invokedMethodsMap = new HashMap<String, MethodInsnNode>();
        private final Map<Integer, InsnNode> insnNodeMap = new HashMap<>();

        public void addInsnNode(InsnNode insnNode) {
            insnNodeMap.put(insnNode.getOpcode(), insnNode);
        }

        public void addMethodInsNode(MethodInsnNode methodInsnNode) {
            final String returnType = Type.getReturnType(methodInsnNode.desc).getClassName();
            invokedMethodsMap.put(returnType, methodInsnNode);
        }

        public void addNewInstanceNode(TypeInsnNode typeInsnNode) {
            newInstanceMap.put(typeInsnNode.desc, typeInsnNode);
        }

        public InsnNode getInsnNode(Integer opcode) {
            return insnNodeMap.get(opcode);
        }

        public TypeInsnNode getTypeInsnNode(String type) {
            return newInstanceMap.get(type);
        }

        public MethodInsnNode getMethodInsNode(String type) {
            return invokedMethodsMap.get(type);
        }
    }
}
