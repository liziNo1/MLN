-- 心动值linXin = LinearLayout(LinearType.HORIZONTAL):width(MeasurementType.MATCH_PARENT):height(14):marginTop(194.5)                                            :setGravity(Gravity.CENTER_HORIZONTAL):cornerRadius(6.8):bgColor(Color(235, 0, 0, 1))tvXinDong = Label():marginLeft(2.5):marginRight(4)                   :textAlign(TextAlign.CENTER)                   :height(MeasurementType.MATCH_PARENT)                    :setGravity(Gravity.CENTER_VERTICAL)                   :width(MeasurementType.WRAP_CONTENT)                   :fontSize(11):textColor(Color(216, 88, 254))                   :text('测试文案'):a_setIncludeFontPadding(false)        --:marginTop(-0.5)linXin:addView(tvXinDong)window:addView(linXin)