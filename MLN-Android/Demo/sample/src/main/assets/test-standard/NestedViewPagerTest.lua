---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by David.
--- DateTime: 2019/1/5 上午10:36
---

screen_w = window:width()
screen_h = window:height()

list = {'https://ss2.baidu.com/-vo3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=a4b3d7085dee3d6d2293d48b252b5910/0e2442a7d933c89524cd5cd4d51373f0830200ea.jpg',
        'https://ss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=a41eb338dd33c895a62bcb3bb72e47c2/5fdf8db1cb134954a2192ccb524e9258d1094a1e.jpg',
        'http://c.hiphotos.baidu.com/image/w%3D400/sign=c2318ff84334970a4773112fa5c8d1c0/b7fd5266d0160924c1fae5ccd60735fae7cd340d.jpg'}

tableView = TableView(true,false)
whiteColor = Color(255,255,255,1)
tableView:width(screen_w):height(screen_h)
tableView:bgColor(whiteColor)
tableView:setRefreshingCallback(function()
    tableView:stopRefreshing()
    tableView:stopLoading()
    -- 刷新数据
    tableView:reloadData()
end)


label2 = Label()
label2:text(" 刷新数据  ")
label2:x(300):y(100)
label2:width(200):height(450)

label2:onClick(
        function()
            tableView:reloadData()
end
)




window:addView(tableView)

window:addView(label2)


tableViewAdapter = TableViewAdapter()
tableViewAdapter:reuseId(function(section,row)
    return "list"
end)

tableViewAdapter:rowCount(function(sectionIdx)
   return 1
end)

tableViewAdapter:heightForCell(function(section, row)
   return 300
end)

tableViewAdapter:initCellByReuseId("list", function(cell)
    print('初始化方法走了多少遍')
    local contentView = cell.contentView
    local width = contentView:width()
    local height = contentView:height()

    local viewPager = ViewPager()

    contentView:addView(viewPager)

    viewPager:enabled(false)

    viewPager:marginLeft(0):marginTop(0):width(width):height(height)
    viewPager:autoScroll(true)
    viewPager:frameInterval(5)
    viewPager:recurrence(true)
    viewPager:showIndicator(true)

    local adapter = ViewPagerAdapter()

    viewPager:adapter(adapter)

    --
    adapter:initCell(function(cell1)
        local contentView = cell1.contentView

        cell1.imageView = ImageView()
        cell1.imageView:bgColor(Color():setHexA(0xE8E8E8,1))
        cell1.imageView:enabled(true)
        cell1.imageView:contentMode(ContentMode.SCALE_TO_FILL)
        cell1.imageView:lazyLoad(false)
        cell1.imageView:marginLeft(0):marginTop(0):width(MeasurementType.MATCH_PARENT):height(MeasurementType.MATCH_PARENT)
        cell1.imageView:onClick(function()
            Navigator:gotoPage(cell1.url)
            local map = Map()
            map:put('event','banner')
            map:put('id',cell1.banner_id)
            MRLogTool:log(map)
        end)
        contentView:addView(cell1.imageView)
    end)

    cell.adapter = adapter
    cell.viewPager = viewPager

end)

tableViewAdapter:fillCellDataByReuseId('list',function(cell, section, row)

    local contentWidth = cell.contentView:width()
    local contentHeight = cell.contentView:height()
    --
    cell.adapter:getCount(function(section)
        if not(list) then
            return 0
        end
        return #list
    end)
    cell.adapter:fillCellData(function(cell1,row)

        local width = cell1.contentView:width()
        local height = cell1.contentView:height()
        cell1.imageView:width(contentWidth):height(contentHeight)
        local item =  list[row]
        cell1.imageView:image(item)
    end)

    cell.viewPager:reloadData()
end)

tableView:adapter(tableViewAdapter)