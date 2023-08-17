	.data
a:
	4
	.text
main:
	load %x0, $a, %x1
	divi %x1, 2, %x2
	beq %x31, %x0, even
	addi %x0, 1, %x10
	end
even:
	subi %x0, 1, %x10
	end