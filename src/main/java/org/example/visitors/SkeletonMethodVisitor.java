package org.example.visitors;

import org.example.models.InvokedInstruction;
import org.example.models.SkeletonMethod;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SkeletonMethodVisitor extends MethodNode {


    private final SkeletonMethod skeletonMethod;

    public SkeletonMethodVisitor(SkeletonMethod skeletonMethod, int access, String name, String descriptor, String signature, String[] exceptions) {
        super(Opcodes.ASM8, access, name, descriptor, signature, exceptions);
        this.skeletonMethod = skeletonMethod;
    }

    @Override
    public void visitEnd() {
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
        skeletonMethod.setInvokedInstructions(insNodesList.stream()
                .map(InvokedInstruction::fromInsNodeList)
                .collect(Collectors.toList()));
    }

}
