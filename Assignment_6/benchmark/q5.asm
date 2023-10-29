    .data
a:
    7
    6
    5
    4
    3
    2
    1
    .text
main:
    addi %x5, 100, %x5
    addi %x10, 200, %x10
    addi %x11, 300, %x11
    addi %x12, 400, %x12
    addi %x13, 500, %x13
    addi %x6, 500, %x6
    addi %x9, 120, %x9
fori:
    store %x9, $a, %x5
    load %x5, $a, %x28
    store %x9, $a, %x10
    load %x5, $a, %x29
    store %x9, $a, %x11
    load %x5, $a, %x30
    store %x9, $a, %x5
    load %x5, $a, %x28
    store %x9, $a, %x10
    load %x5, $a, %x29
    store %x9, $a, %x11
    load %x5, $a, %x30
    store %x9, $a, %x12
    load %x5, $a, %x22
    store %x9, $a, %x13
    load %x5, $a, %x25
    load %x5, $a, %x28
    store %x9, $a, %x10
    load %x5, $a, %x29
    store %x9, $a, %x11
    load %x5, $a, %x30
    store %x9, $a, %x5
    load %x5, $a, %x28
    store %x9, $a, %x10
    load %x5, $a, %x29
    store %x9, $a, %x11
    load %x5, $a, %x30
    store %x9, $a, %x12
    load %x5, $a, %x22
    store %x9, $a, %x13
    load %x5, $a, %x25
    addi %x5, 1, %x5
    blt %x5, %x6, fori
    end
