import { getUserSignInRecord } from '@/services/xiaoxinshu/userController';
import { message } from 'antd';
import dayjs from 'dayjs';
import ReactECharts from 'echarts-for-react';
import React, { useEffect, useState } from 'react';

/**
 * 刷题日历图
 * @constructor
 */
const CalendarChart: React.FC = () => {
  // 签到日期列表（[1, 200]，表示第 1 和第 200 天有签到记录）
  const [dataList, setDataList] = useState<number[]>([]);
  // 当前年份
  const year = new Date().getFullYear();

  // 计算图表所需的数据
  const optionsData = dataList.map((dayOfYear) => {
    // 计算日期字符串
    const dateStr = dayjs(`${year}-01-01`)
      .add(dayOfYear - 1, 'day')
      .format('YYYY-MM-DD');
    return [dateStr, 1];
  });

  const options = {
    visualMap: {
      show: false,
      min: 0,
      max: 1,
      inRange: {
        color: ['#efefef', 'lightgreen'],
      },
    },
    calendar: {
      range: year,
      left: 20,
      cellSize: ['auto', 16],
      yearLabel: {
        position: 'top',
        formatter: `${year} 年刷题记录`,
      },
    },
    series: {
      type: 'heatmap',
      coordinateSystem: 'calendar',
      data: optionsData,
    },
  };

  // 请求后端获取数据
  const fetchDataList = async () => {
    try {
      const res = await getUserSignInRecord({
        year,
      });
      setDataList(res.data || []);
    } catch (e: any) {
      message.error('获取刷题签到记录失败，' + e.message);
    }
  };

  useEffect(() => {
    fetchDataList();
  }, []);

  return <ReactECharts className="calendar-chart" option={options} />;
};

export default CalendarChart;
