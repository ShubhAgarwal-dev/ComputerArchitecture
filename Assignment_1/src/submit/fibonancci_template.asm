	.data
n:
	10
	.text
main:
	load %x0, $n, %x3
	addi %x0, 65535, %x4
	addi %x0, 0, %x5
	addi %x0, 1, %x6
	store %x5, $n, %x4
	subi %x4, 1, %x4
	subi %x3, 1, %x3
	store %x6, $n, %x4
	subi %x4, 1, %x4
	subi %x3, 1, %x3
loopFib:
	beq %x3, 0, success
	add %x5, %x6, %x7
	store %x7, $n, %x4
	subi %x4, 1, %x4
	subi %x3, 1, %x3
	add %x0, %x6, %x5
	add %x0, %x7, %x6
	jmp loopFib
success:
	end