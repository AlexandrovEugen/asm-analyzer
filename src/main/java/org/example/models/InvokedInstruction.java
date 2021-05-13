package org.example.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LineNumberNode;

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
        final AbstractInsnNode abstractInsnNode = insNodes.get(0);
        abstractInsnNode.getType();
        if (abstractInsnNode.getType() == AbstractInsnNode.LINE) {
            LineNumberNode lineNumberNode = (LineNumberNode) abstractInsnNode;
            invokedInstruction.setLine(lineNumberNode.line);
        }
        return invokedInstruction;
    }
}
