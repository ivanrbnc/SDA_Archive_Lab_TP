import random

b = int(input('Banyak gedung: '))
p = int(input('Banyak perintah: '))
charset = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
gedung = []
print(b)
for i in range(b):
    temp = (charset[i], random.randint(1, 5))
    print(*temp)
    gedung.append(temp)

for _ in range(2):
    start = random.choice(gedung)
    print(start[0], random.randint(1, start[1]))

print(p)
for _ in range(p):
    print(random.choice(['GERAK', 'PINDAH', 'HANCUR', 'TAMBAH']))
