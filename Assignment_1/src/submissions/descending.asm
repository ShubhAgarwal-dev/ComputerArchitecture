.data
a:
    70
    40
    80
    100
    90
n:
    5
.text
main:
    load %x0, $n, %x3       
    load %x0, $a, %x4       
    addi %x0, 1, %x5        
    addi %x0, 0, %x10
    subi %x3, 1, %x11       
outerloop:
    beq %x5, %x3, doneouter 
    addi %x0, 0, %x7     
innerloop:
    beq %x7, %x3, doneinner
    load %x7, $a, %x6
    addi %x7, 1, %x8
    load %x8, $a, %x9
    bgt %x9, %x6, swap
    addi %x7, 1, %x7
    jmp innerloop
swap:
    add %x6, %x0, %x10
    store %x6, 0, %x8
    store %x9, 0, %x7
    addi %x7, 1, %x7
    jmp innerloop
doneinner:
    addi %x5, 1, %x5
    jmp outerloop
doneouter:
    store %x0, 0, %x3
    end