---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by XiongFangyu.
--- DateTime: 2019-06-28 17:40
---

pt = require("print_table")
pt.print_r(getmetatable(luajava))

luajava.bindClass('aaaaa','bbb',1)

local u = UDIndex()
print('ud metatable:')
pt.print_r(getmetatable(u))
print('ud metatable.metatable:')
pt.print_r(getmetatable(getmetatable(u)))
u:aaa('bbb', 1)

u['bbb'] = 1