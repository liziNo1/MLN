---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by xujingyu.
--- DateTime: 2019/4/25 11:01
--- 设置圆角半径需根据view最小尺寸进行相应限制
---
W = window:width()
H = window:height()
scrollView = ScrollView(false, true):width(W):height(H)

view1 = View():bgColor(Color(255, 0, 0, 0.5))
              :marginTop(100):width(150):height(100)
              :cornerRadius(150)
scrollView:addView(view1)

view2 = View():bgColor(Color(255, 0, 0, 0.5))
              :marginTop(10):width(150):height(100)
              :setCornerRadiusWithDirection(150, RectCorner.TOP_LEFT)
scrollView:addView(view2)

view3 = View():bgColor(Color(255, 0, 0, 0.5))
              :marginTop(10):width(150):height(100)
              :setCornerRadiusWithDirection(150, MBit:bor(RectCorner.TOP_RIGHT, RectCorner.BOTTOM_LEFT))
scrollView:addView(view3)

view4 = View():bgColor(Color(255, 0, 0, 0.5))
              :marginTop(10):width(150):height(100)
              :setCornerRadiusWithDirection(150, MBit:bor(RectCorner.TOP_LEFT, RectCorner.TOP_RIGHT, RectCorner.BOTTOM_LEFT))
scrollView:addView(view4)

view4 = View():bgColor(Color(255, 0, 0, 0.5))
              :marginTop(10):width(150):height(100)
              :addCornerMask(150, Color(255, 255, 0, 0.5), RectCorner.TOP_LEFT)
scrollView:addView(view4)

view5 = View():bgColor(Color(255, 0, 0, 0.5))
              :marginTop(10):width(150):height(100)
              :addCornerMask(150, Color(255, 255, 0, 0.5), MBit:bor(RectCorner.TOP_RIGHT, RectCorner.BOTTOM_LEFT))
scrollView:addView(view5)

view6 = View():bgColor(Color(255, 0, 0, 0.5))
              :marginTop(10):width(150):height(100)
              :addCornerMask(150, Color(255, 255, 0, 0.5), MBit:bor(RectCorner.TOP_LEFT, RectCorner.BOTTOM_LEFT, RectCorner.BOTTOM_RIGHT))
scrollView:addView(view6)

myView = View()
myView:bgColor(Color(255,0,0,1))
myView:setGravity(Gravity.CENTER):width(100):height(150)
-- myView:cornerRadius(150)
-- myView:setCornerRadiusWithDirection(150, RectCorner.TOP_LEFT + RectCorner.TOP_RIGHT)
myView:addCornerMask(150, Color(255,255,255,1),RectCorner.TOP_LEFT + RectCorner.TOP_RIGHT + RectCorner.BOTTOM_RIGHT)
scrollView:addView(myView)

-- window:onClick(function()
-- myView:setCornerRadiusWithDirection(10, RectCorner.TOP_LEFT + RectCorner.TOP_RIGHT)
-- end)

window:onClick(function()
    myView:width(150)
    -- myView:addCornerMask(10, Color(255,255,255,1),RectCorner.TOP_LEFT + RectCorner.TOP_RIGHT + RectCorner.BOTTOM_RIGHT)
end)
window:addView(scrollView)