from sympy import symbols, solve

with open("input.txt") as f:
    points = []
    for line in f.read().strip().splitlines():
        x = [int(n) for n in line.split(" @ ")[0].split(", ")]
        v = [int(n) for n in line.split(" @ ")[1].split(", ")]
        points.append((*x, *v))

x = symbols('x')
y = symbols('y')
z = symbols('z')
vx = symbols('vx')
vy = symbols('vy')
vz = symbols('vz')

x1, y1, z1, vx1, vy1, vz1 = points[0]
x2, y2, z2, vx2, vy2, vz2 = points[1]
x3, y3, z3, vx3, vy3, vz3 = points[2]

sols = solve(
    [
        (x - x1) * (vy - vy1) - (y - y1) * (vx - vx1),
        (y - y1) * (vz - vz1) - (z - z1) * (vy - vy1),
        (x - x2) * (vy - vy2) - (y - y2) * (vx - vx2),
        (y - y2) * (vz - vz2) - (z - z2) * (vy - vy2),
        (x - x3) * (vy - vy3) - (y - y3) * (vx - vx3),
        (y - y3) * (vz - vz3) - (z - z3) * (vy - vy3)
    ],
    [x, y, z, vx, vy, vz], dict=True)

# select solution with integer speed components
for s in sols:
    if s[vx] == int(s[vx]) and s[vy] == int(s[vy]) and s[vz] == int(s[vz]):
        print(s)
        break
print(s[x] + s[y] + s[z])
