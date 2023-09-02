	.data
n:
	10
	.text
main:
    beq %x0, %x1, main
    beq %x0, %x1, loop
    end
loop:
    end