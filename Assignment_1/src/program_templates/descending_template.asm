	.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
	load %x0, $n, %x1
	addi %x0, 1, %x3
	beq %x1, %x3, endp
	add %x0, %x0, %x2
loop:
	load %x2, $a, %x4
	load %x3, $a, %x5
	blt %x4, %x5, exchange
	addi %x2, 1, %x2
	addi %x3, 1, %x3
	beq %x3, %x1, endcheck
	jmp loop
exchange:
	store %x4, $a, %x3
	store %x5, $a, %x2
	addi %x0, 1, %x6
endcheck:
	beq %x6, %x0, endp
	add %x0, %x0, %x2
	addi %x0, 1, %x3
	add %x0, %x0, %x6
	jmp loop
endp:
	end
