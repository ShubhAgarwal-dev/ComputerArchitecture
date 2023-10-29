    .data
a:
    500
    .text
main:
    load %x0, $a, %x4
    addi %x4, 10, %x4
    addi %x6, 6, %x6
    subi %x4, 1, %x4
    addi %x7, 2, %x7
    addi %x9, 2, %x9
loopi:
    subi %x6, 1, %x6
    muli %x7, 2, %x7
    muli %x7, 2, %x7
    muli %x9, 2, %x9
    muli %x7, 2, %x7
    muli %x7, 2, %x7
    muli %x9, 2, %x9
    muli %x9, 2, %x9
    muli %x7, 2, %x7
    muli %x9, 2, %x9
    bgt %x6, %x0, loopi
    end