	.data
a:
	10
	.text
main:
	load %x0, $a, %x3
	divi %x3, 2, %x3
	muli %x3, 2, %x3
	load %x0, $a, %x4
	beq %x3, %x4, success
	addi %x0, 1, %x10
	end
success:
	subi %x0, 1, %x10
	end
