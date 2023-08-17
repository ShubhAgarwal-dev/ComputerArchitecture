	.data
a:
	10
	.text
main:
	load %x0, $a, %x1
	addi %x0, 1, %x2
	beq %x1, %x2, is_prime
	addi %x2, 1, %x2
loop:
	beq %x1, %x2, prime
	div %x1, %x2, %x3
	beq %x0, %x31, notprime
	addi %x2, 1, %x2
	jmp loop
prime:
	addi %x0, 1, %x10
	end
notprime:
	subi %x0, 1, %x10
	end