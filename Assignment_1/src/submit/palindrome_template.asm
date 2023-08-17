	.data
a:
	12321
	.text
main:
	load %x0, $a, %x3 
	load %x0, $a, %x4
	addi %x0, 0, %x5
	addi %x0, 1, %x6
loopPall:
	beq $x4, 0, checkPall
	divi $x4, 10, $x4
	mul $x31, $x6, $x7
	add $x5, $x7, $x5
	muli %x6, 10, %x6
	jmp loopPall
checkPall:
	beq $x3, $x5, passPall
	subi $x0, 1, $x10
	end
passPall:
	addi $x0, 1, $x10
	end