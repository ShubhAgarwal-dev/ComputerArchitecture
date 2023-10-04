package processor.pipeline;

import processor.Processor;

public class Execute {
    Processor containingProcessor;
    OF_EX_LatchType OF_EX_Latch;
    EX_MA_LatchType EX_MA_Latch;
    EX_IF_LatchType EX_IF_Latch;

    public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch) {
        this.containingProcessor = containingProcessor;
        this.OF_EX_Latch = oF_EX_Latch;
        this.EX_MA_Latch = eX_MA_Latch;
        this.EX_IF_Latch = eX_IF_Latch;
    }

    /**
     * {@code @params} opCode,op1,op2,imm
     *
     * @return op1 [operation] op2/imm
     * @Works_For: opcode>=1 opcode<=7
     */
    private long performArithmetic(int opCode, int op1, int op2, int imm) {
        if (opCode == 0) {
            return (long) op1 + op2;
        } else if (opCode == 1) {
            return (long) op1 + imm;
        } else if (opCode == 2) {
            return (long) op1 - op2;
        } else if (opCode == 3) {
            return (long) op1 - imm;
        } else if (opCode == 4) {
            return (long) op1 * op2;
        } else if (opCode == 5) {
            return (long) op1 * imm;
        } else if (opCode == 6) {
            return (long) op1 / op2; // return remainder too
        } else if (opCode == 7) {
            return (long) op1 / imm; // return remainder too
        } else {
            throw new Error("Undefined OpCode");
        }
    }

    /**
     * {@code @prarms:} opCode,op1,op2,imm
     *
     * @return op1 [operation] op2/imm
     * @Works_For: opcode>=8 opcode<=13
     */
    private long performLogical(int opCode, int op1, int op2, int imm) {
        if (opCode == 8) {
            return (long) op1 & op2;
        } else if (opCode == 9) {
            return (long) op1 & imm;
        } else if (opCode == 10) {
            return (long) op1 | op2;
        } else if (opCode == 11) {
            return (long) op1 | imm;
        } else if (opCode == 12) {
            return (long) op1 ^ op2;
        } else if (opCode == 13) {
            return (long) op1 ^ imm;
        } else {
            throw new Error("Undefined OpCode");
        }
    }

    /**
     * {@code @prarms:} opCode,op1,op2,imm
     *
     * @return op1 [operation] op2/imm
     * @Works_For: opcode>=16 opcode<=21
     */
    private long performShift(int opCode, int op1, int op2, int imm) {
        if (opCode == 16) {
            return (long) op1 << op2;
        } else if (opCode == 17) {
            return (long) op1 << imm;
        } else if (opCode == 18) {
            return (long) op1 >> op2;
        } else if (opCode == 19) {
            return (long) op1 >> imm;
        } else if (opCode == 20) {
            return (long) op1 >>> op2;
        } else if (opCode == 21) {
            return (long) op1 >>> imm;
        } else {
            throw new Error("Undefined OpCode");
        }
    }

    private void setR31Register(int overFlow, int underFlow, int remainder, int op1) {
        if (overFlow != 0) {
            EX_MA_Latch.setR31(overFlow);
        }
        if (underFlow != -1) {
            EX_MA_Latch.setR31(op1 << (32 - underFlow));
        }
        if (remainder != -1) {
            EX_MA_Latch.setR31(remainder);
        }
    }

    private void handelBranchTaken(int opCode, int op1, int op2) {
        if (opCode == 24) {
            containingProcessor.setBranchTaken(true);
        } else if (opCode == 25) {
            containingProcessor.setBranchTaken(op1 == op2);
        } else if (opCode == 26) {
            containingProcessor.setBranchTaken(op1 != op2);
        } else if (opCode == 27) {
            containingProcessor.setBranchTaken(op1 < op2);
        } else if (opCode == 28) {
            containingProcessor.setBranchTaken(op1 > op2);
        }
    }

    public void performEX() {
        if (OF_EX_Latch.isBubble()){
            return;
        }

        int opCode = OF_EX_Latch.getOpCode();
        int immediate = OF_EX_Latch.getImmediate();
        int op1 = OF_EX_Latch.getR1();
        int op2 = OF_EX_Latch.getR2();
        int rd = OF_EX_Latch.getRd();
        if (opCode == 30) {
            containingProcessor.getDataLockUnit().setInstEXString(Integer.toBinaryString(opCode) + "0".repeat(32 - Integer.toBinaryString(opCode).length()));
        }
        if (opCode != 30) {
            containingProcessor.getDataLockUnit().setInstEXString(OF_EX_Latch.getInstruction());
            EX_MA_Latch.setInstruction(OF_EX_Latch.getInstruction());
            EX_MA_Latch.setR31(-1);
            long calcOpRes = 0;
            long remainder = -1;
            long underflow = -1;

            // getThe results
            if (opCode >= 0 && opCode <= 7) {
                calcOpRes = performArithmetic(opCode, op1, op2, immediate);
                if (opCode == 6 || opCode == 7) {
                    remainder = (opCode == 6) ? op1 % op2 : op1 % immediate;
                }
            } else if (opCode >= 8 && opCode <= 13) {
                calcOpRes = performLogical(opCode, op1, op2, immediate);
            } else if (opCode == 14 || opCode == 15) {
                if (opCode == 14) {
                    calcOpRes = (op1 < op2) ? 1 : 0;
                } else {
                    calcOpRes = (op1 < immediate) ? 1 : 0;
                }
            } else if (opCode >= 16 && opCode <= 21) {
                calcOpRes = performShift(opCode, op1, op2, immediate);
                if (opCode >= 18) {
                    underflow = (opCode == 18 || opCode == 20) ? op2 : immediate;
                }
            } else if (opCode == 22) {
                calcOpRes = op1 + immediate;
            } else if (opCode == 23) {
                calcOpRes = op2 + immediate;
            } else {
                handelBranchTaken(opCode, op1, op2);
            }

            int opRes = (int) calcOpRes;
            int overflow = (int) (calcOpRes >> 32);

            // passing data to latch
            setR31Register(overflow, (int) underflow, (int) remainder, op1);

            // System.out.println("[Debug] (EX) ALU Result: " + opRes);
            // System.out.println("[Debug] (EX) r31: " + EX_MA_Latch.getR31());
            // System.out.println("[Debug] (EX) isBranchTaken: " +
            // containingProcessor.isBranchTaken());
            EX_MA_Latch.setOp1(op1);
            EX_MA_Latch.setOp2(op2);
            EX_MA_Latch.setOpRes(opRes);

            // enable disable latches
        } else {
            EX_MA_Latch.setOp1(op1);
            EX_MA_Latch.setOp2(op2);
            EX_MA_Latch.setOpRes(0);
            // enable disable latches
        }
        EX_MA_Latch.setOpCode(opCode);
        EX_MA_Latch.setRd(rd);
        EX_MA_Latch.setMA_enable(true);
        OF_EX_Latch.setEX_enable(false);
    }

}
