	.data
a:
	11
	.text
main:
	load %x0, $a, %x4
	add %x0, %x0, %x10
	addi %x0, 2, %x5
	beq %x5, %x4, isprime
loop:
	div %x4, %x5, %x6
	beq %x31, %x0, notprime
	addi %x5, 1, %x5
	beq %x5, %x4, isprime
	jmp loop
isprime:
	addi %x10, 1, %x10
	end
notprime:
	subi %x10, 1, %x10
	end