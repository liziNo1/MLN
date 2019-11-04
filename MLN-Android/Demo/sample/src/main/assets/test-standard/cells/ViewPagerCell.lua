---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by momo.
--- DateTime: 2018/8/31 下午6:35
---
local _class = {}
_class._version = '1.0'
_class._classname = 'ViewPagerCell'

function ViewPagerCell()
    return _class
end

function _class:onCreate(cell)


    cell.bgImage = ImageView():contentMode(ContentMode.SCALE_ASPECT_FIT)
    cell.contentView:cornerRadius(8)

    cell.contentView:bgColor(Color(255,0,0,0.5))
    cell.contentView:addView(cell.bgImage)
end

function _class:onLayout(cell, iconName, pos)
    -- cell.bgImage:width(cell.contentView:width()):height(cell.contentView:height())
     cell.bgImage:width(200):height(200)
    cell.bgImage:image(iconName)
end

return _class
