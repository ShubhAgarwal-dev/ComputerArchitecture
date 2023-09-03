	.data
a:
	10
b:
	20
	.text
main:
    jmp loop
    end
loop:
    load %x0, $a, %x3
    end