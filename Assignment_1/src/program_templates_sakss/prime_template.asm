	.data
a:
	10
	.text
main:
	load %x0, $a, %x3
	load %x0, $a, %x4
loop:
	beq %x4, 1, pass
	subi %x4, 1, %x4
	div %x3, %x4, %x5
	beq %x31, 0, fail
	jmp loop
pass:
	addi %x0, 1, %x10
	end
fail:
	subi %x0, 1, %x10
	end