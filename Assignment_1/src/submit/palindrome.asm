	.data
a:
	1111
	.text
main:
	load %x0, $a, %x3 
	load %x0, $a, %x4
	addi %x0, 0, %x5
loopPall:
	beq $x4, 0, checkPall
	divi $x4, 10, $x4
	muli %x5, 10, %x5
	add $x5, $x31, $x5
	jmp loopPall
checkPall:
	beq $x3, $x5, passPall
	subi $x0, 1, $x10
	end
passPall:
	addi $x0, 1, $x10
	end