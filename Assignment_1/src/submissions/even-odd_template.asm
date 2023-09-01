	.data
a:
	11
	.text
main:
	load %x0, $a, %x4
	add %x0, %x0, %x10
	divi %x4, 2, %x6
	beq %x31, 0, even
	beq %x31, 1, odd
odd:
	addi %x10, 1, %x10
	end
even:
	subi %x10, 1, %x10
	end