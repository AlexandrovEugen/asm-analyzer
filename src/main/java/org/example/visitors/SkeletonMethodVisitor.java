package org.example.visitors;

import org.example.handlers.InsnHandler;
import org.example.models.Instruction;
import org.example.models.SkeletonMethod;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

public class SkeletonMethodVisitor extends MethodNode {


    private final SkeletonMethod skeletonMethod;

    public SkeletonMethodVisitor(SkeletonMethod skeletonMethod, int access, String name, String descriptor, String signature, String[] exceptions) {
        super(Opcodes.ASM8, access, name, descriptor, signature, exceptions);
        this.skeletonMethod = skeletonMethod;
    }

    @Override
    public void visitEnd() {
        final InsnHandler insnHandler = new InsnHandler(instructions);
        final List<Instruction> instructions = insnHandler.getInstructions();
        skeletonMethod.setInstructions(instructions);
    }

}
