	.data
a:
	10
	.text
main:
    load %x0, $a, %x1
    divi %x4, 2, %x2
    beq %x31, %x0, even
    addi %x0, -1, %x10
    end
even:
    addi %x0 , 1 , %x10
    end
