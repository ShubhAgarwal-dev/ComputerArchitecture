	.data
a:
	101
	.text
main:
	load %x0, $a, %x3
	load %x0, $a, %x4
	add %x0, %x0, %x5
	add %x0, %x0, %x6
	add %x0, %x0, %x10
loop:
	beq %x4, 0, reverse
	divi %x4, 10, %x4
	add %x0, %x31, %x5
	muli %x6, 10, %x6
	add %x6, %x5, %x6
	jmp loop
reverse:
	beq %x6, %x3, palind
	subi %x0, 1, %x10
	end
palind:
	addi %x10, 1, %x10
	end