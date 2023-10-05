package processor;

import processor.pipeline.OF_EX_LatchType;

/**
 * DataLock
 */
public class DataLock {

    public DataLock() {}

    private String instOFString = Integer.toBinaryString(30) + "0".repeat(28);
    private String instEXString = Integer.toBinaryString(30) + "0".repeat(28);
    private String instMAString = Integer.toBinaryString(30) + "0".repeat(28);
    private String instRWString = Integer.toBinaryString(30) + "0".repeat(28);

    private boolean conflict;

    public boolean getConflict() {
        return conflict;
    }

    public void setInstOFString(String instOFString) {
        this.instOFString = instOFString;
    }

    // Setter

    public void setInstEXString(String instEXString) {
        this.instEXString = instEXString;
    }

    public void setInstMAString(String instMAString) {
        this.instMAString = instMAString;
    }

    public void setInstRWString(String instRWString) {
        this.instRWString = instRWString;
    }

    private int getOperation(String instructionString) {
        System.out.println(instructionString);
        return Integer.parseInt(instructionString.substring(0, 5), 2);
    }

    // Operand Getter

    private int getOP1(String instructioString) {
        return Integer.parseInt(instructioString.substring(5, 10), 2);
    }

    private int getOP2(String instructioString) {
        return Integer.parseInt(instructioString.substring(10, 15), 2);
    }

    private int getOP3(String instructioString) {
        return Integer.parseInt(instructioString.substring(15, 20), 2);
    }

    private boolean compare_R3_R3(String instructionString1, String instructionString2) {
        int I1_rs1, I1_rs2, I1_rd;
        int I2_rd;
        I1_rs1 = getOP1(instructionString1);
        I1_rs2 = getOP2(instructionString1);
        I1_rd = getOP3(instructionString1);
        I2_rd = getOP3(instructionString2);
        if (I1_rs1 == I2_rd || I1_rs2 == I2_rd || I1_rd == I2_rd) {
            System.out.println("[DL] Debug: Comparing R3_R3: False");
            return false;
        }
        System.out.println("[DL] Debug: Comparing R3_R3: True");
        return true;
    }

    // Compare Logic

    // ! Store instruction special case where the op1 is needed
    private boolean compare_R2I_R2I(String instructionString1, String instructionString2) {
        int I1_rs1, I1_rd;
        I1_rs1 = getOP1(instructionString1);
        I1_rd = getOP2(instructionString1);
        if (getOperation(instructionString2) != 23) {
            int I2_rd;
            I2_rd = getOP2(instructionString2);
            if (I1_rs1 == I2_rd || I1_rd == I2_rd) {
                System.out.println("[DL] Debug: Comparing R2I_R2I(Non Store): False");
                return false;
            }
            System.out.println("[DL] Debug: Comparing R2I_R2I(Non Store): True");
            return true;
        } else {
            int I2_rs1;
            I2_rs1 = getOP1(instructionString2);
            if (I1_rs1 == I2_rs1 || I1_rd == I2_rs1) {
                System.out.println("[DL] Debug: Comparing R2I_R2I(Store): False");
                return false;
            }
            System.out.println("[DL] Debug: Comparing R2I_R2I(Store): True");
            return true;
        }
    }

    // ! Store instruction special case where the op1 is needed
    private boolean compare_R3_R2I(String instructionString1, String instructionString2) {
        int I1_rs1, I1_rs2, I1_rd;
        I1_rs1 = getOP1(instructionString1);
        I1_rs2 = getOP2(instructionString1);
        I1_rd = getOP3(instructionString1);
        if (getOperation(instructionString2) != 23) {
            int I2_rd;
            I2_rd = getOP2(instructionString2);
            if (I1_rs1 == I2_rd || I1_rs2 == I2_rd || I1_rd == I2_rd) {
                System.out.println("[DL] Debug: Comparing R3_R2I(Non Store): False");
                return false;
            }
            System.out.println("[DL] Debug: Comparing R3_R2I(Non Store): True");
            return true;
        } else {
            int I2_rs1;
            I2_rs1 = getOP1(instructionString2);
            if (I1_rs1 == I2_rs1 || I1_rs2 == I2_rs1 || I1_rd == I2_rs1) {
                System.out.println("[DL] Debug: Comparing R3_R2I(Store): False");
                return false;
            }
            System.out.println("[DL] Debug: Comparing R3_R2I(Store): True");
            return true;
        }

    }

    private boolean compare_R2I_R3(String instructionString1, String instructionString2) {
        int I1_rs1, I1_rd;
        I1_rs1 = getOP1(instructionString1);
        I1_rd = getOP2(instructionString1);
        int I2_rd;
        I2_rd = getOP2(instructionString2);
        if (I1_rs1 == I2_rd || I1_rd == I2_rd) {
            System.out.println("[DL] Debug: Comparing R2I_R3: False");
            return false;
        }
        System.out.println("[DL] Debug: Comparing R2I_R3: True");
        return true;
    }

    /*
     * If OF has RI then no need to check the type (R3 or R2I) of the other
     * instruction
     */
    private boolean compare_RI_RX(String instructionString1, String instructionString2) {
        int I1_rd = getOP1(instructionString1);
        int I2_rd = getOP2(instructionString2);
        if (I1_rd == I2_rd) {
            System.out.println("[DL] Debug: Comparing RI_RX: False");
            return false;
        }
        System.out.println("[DL] Debug: Comparing RI_RX: True");
        return true;
    }

    private boolean check_OF_X(int operationOF, String instructionString) {
        int operationEX = getOperation(instructionString);
        if (operationEX >= 24 && operationEX <= 30) {
            return true;
        }
        if (operationOF >= 0 && operationOF <= 21 && operationOF % 2 == 0) {
            if (operationEX >= 0 && operationEX <= 21 && operationEX % 2 == 0) {
                // R3_R3
                return compare_R3_R3(instOFString, instructionString);
            } else if (operationEX >= 0 && operationEX <= 21 && operationEX % 2 != 0) {
                // R3_R2I
                return compare_R3_R2I(instOFString, instructionString);
            } else if (operationEX >= 22 && operationEX <= 23) {
                // R3_R2I
                return compare_R3_R2I(instOFString, instructionString);
            } else {
                return false;
            }
        } else if (operationOF >= 0 && operationOF <= 21 && operationOF % 2 != 0) {
            if (operationEX >= 0 && operationEX <= 21 && operationEX % 2 == 0) {
                // R2I_R3
                return compare_R2I_R3(instOFString, instructionString);
            } else if (operationEX >= 0 && operationEX <= 21 && operationEX % 2 != 0) {
                // R2I_R2I
                return compare_R2I_R2I(instOFString, instructionString);
            } else if (operationEX >= 22 && operationEX <= 23) {
                // R2I_R2I
                return compare_R2I_R2I(instOFString, instructionString);
            } else {
                return false;
            }
        } else if (operationOF >= 22 && operationOF <= 23) {
            if (operationEX >= 0 && operationEX <= 21 && operationEX % 2 == 0) {
                // R2I_R3
                return compare_R2I_R3(instOFString, instructionString);
            } else if (operationEX >= 0 && operationEX <= 21 && operationEX % 2 != 0) {
                // R2I_R2I
                return compare_R2I_R2I(instOFString, instructionString);
            } else if (operationEX >= 22 && operationEX <= 23) {
                // R2I_R2I
                return compare_R2I_R2I(instOFString, instructionString);
            } else {
                return false;
            }
        } else if (operationOF >= 25 && operationOF <= 28) {
            if (operationEX >= 0 && operationEX <= 21 && operationEX % 2 == 0) {
                // R2I_R3
                return compare_R2I_R3(instOFString, instructionString);
            } else if (operationEX >= 0 && operationEX <= 21 && operationEX % 2 != 0) {
                // R2I_R2I
                return compare_R2I_R2I(instOFString, instructionString);
            } else if (operationEX >= 22 && operationEX <= 23) {
                // R2I_R2I
                return compare_R2I_R2I(instOFString, instructionString);
            } else {
                return false;
            }
        } else if (operationOF == 24) {
            return compare_RI_RX(instOFString, instructionString);
        }
        return false;
    }

    // Checks

    private boolean isConflict() {
        int operationOF = getOperation(instOFString);
        if (check_OF_X(operationOF, instEXString)) {
            System.out.println("[DL] Debug: Conflict in OF and EX");
            return true;
        } else if (check_OF_X(operationOF, instMAString)) {
            System.out.println("[DL] Debug: Conflict in OF and MA");
            return true;
        } else if (check_OF_X(operationOF, instRWString)) {
            System.out.println("[DL] Debug: Conflict in OF and RW");
            return true;
        }
        return false;
    }

    public void setConflict(boolean conflict) {
        this.conflict = conflict;
    }

    public void applyDataLock(OF_EX_LatchType OF_EX_Latch) {
        if (isConflict()) {
            OF_EX_Latch.setOpCode(30);
            System.out.println("[DL] Debug: Pipeline Stalled");
        }
        System.out.println("[DL] Debug: All Checks Passed No Stall");
    }
}