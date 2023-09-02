	.data
n:
	10
	.text
main:
	load %x0, $n, %x4
	bgt %x0, %x4, notpossible
	addi %x0, 2, %x5
	addi %x0, 0, %x6
	addi %x0, 1, %x7
	add %x0, %x0, %x8
	add %x0, %x0, %x10
	addi %x0, 65535, %x11
	beq %x4, %x5, basecase
	blt %x4, %x5, notpossible
	store %x6, 0, %x11
	subi %x11, 1, %x11
	store %x7, 0, %x11
	subi %x11, 1, %x11
loop:
	beq %x5, %x4, done
	add %x6, %x7, %x8
	subi %x7, 0, %x6
	addi %x8, 0, %x7
	store %x7, 0, %x11
	subi %x11, 1, %x11
	addi %x5, 1, %x5
	jmp loop
notpossible:
	subi %x0, 1, %x6
	store %x6, 0, %x11
	end
basecase:
	store %x6, 0, %x11
	end
done:
	subi %x0, 1, %x10
	end