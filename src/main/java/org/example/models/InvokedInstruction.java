package org.example.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class InvokedInstruction {
    private int line;
    private String invokedMethodName;
    private List<InvokedMethodParam> invokedMethodParams = new ArrayList<>();

    public static InvokedInstruction fromInsNodeList(final List<AbstractInsnNode> insNodes) {
        final InvokedInstruction invokedInstruction = new InvokedInstruction();
        for (AbstractInsnNode insNode : insNodes) {
            handleInsnNode(insNode, invokedInstruction);
        }
        return invokedInstruction;
    }

    private static void handleInsnNode(AbstractInsnNode insNode, InvokedInstruction invokedInstruction) {
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
                invokedInstruction.setLine(lineNumberNode.line);
                break;
            case AbstractInsnNode.LOOKUPSWITCH_INSN:
                LookupSwitchInsnNode lookupSwitchInsnNode = (LookupSwitchInsnNode) insNode;
                break;
            case AbstractInsnNode.METHOD_INSN:
                break;
            case AbstractInsnNode.TABLESWITCH_INSN:
                TableSwitchInsnNode tableSwitchInsnNode = (TableSwitchInsnNode) insNode;
                break;
            case AbstractInsnNode.TYPE_INSN:
                TypeInsnNode typeInsnNode = (TypeInsnNode) insNode;
                break;
            case AbstractInsnNode.VAR_INSN:
                VarInsnNode varInsnNode = (VarInsnNode) insNode;
                break;
        }
    }
}
