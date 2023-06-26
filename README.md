# 聚合搜索平台

## 项目介绍

一个企业级聚合搜索平台（搜索中台）。**允许用户在同一个页面集中搜索出不同来源、不同类型的内容，提升用户的检索效率和搜索体验**。当企业中有多个项目的数据需要被搜索时，无需针对每个项目单独开发搜索功能，可以直接将数据接入搜索中台，提升开发效率。例如b站

![](D:\download\javaFrame\GinSo\img\无标题.png)

## 技术选型

### 前端

- Vue
- ant Design Vue
- Lodash

- #### 后端

  - Spring Boot 2.7 框架
  - MySQL 数据库（8.x 版本）
  - Elastic Stack
    - Elasticsearch 搜索引擎（重点）
    - Logstash 数据管道
    - Kibana 数据可视化
  - 数据抓取（jsoup、HttpClient 爬虫）
    - 离线
    - 实时
  - 设计模式
    - 门面模式
    - 适配器模式
    - 注册器模式
  - 数据同步（4 种同步方式）
    - 定时
    - 双写
    - Logstash
    - Canal
  - JMeter 压力测试

## 业务流程

1. 先得到各种不同分类的数据
2. 提供一个搜索页面（单一搜索 + 聚合搜索），支持搜索

可以去做一些优化，比如关键词高亮、防抖节流。

![image-20230326095708531](https://typora-1313423481.cos.ap-guangzhou.myqcloud.com/typora/image-20230326095708531.png) 

## 内容来源

- 数据库
- 爬虫
  - 直接请求接口（获取接口信心，分离出有用的数据）
  - 从渲染的网页根据特定标签获取数据



## 前端解决问题

- **用url记录用户搜索状态，实现页面的半持久化**

目标：用 url 记录页面搜索状态，当用户刷新页面时，能够从 url 还原之前的搜索转态需要双向同步：url <=> 页面状态

**核心小技巧**：把同步状态改为单向，即只允许 url 来改变页面状态，不允许反向

> 监听搜索内容，只维护一个变量

## 后端解决的问题

- 后端并发聚合查询

## 统一的作用

- 提高前端的性能
- 提高整体的性能
- 节约前端的请求

## 设计模式

- 设配器模式

> 定义统一数据源规范

- 门面模式

> 提供向外界直接调用的接口，不需要管理细节。以一种简单的方式管理子系统

- 注册器模式

> 将实现父类对象注册到集合中，让子类决定使用那个类。

## 数据同步

数据存放在MySQL中，（当有数据变更时）需要和ES进行数据同步，保证数据一致（MySQL为主）

`全量同步`（首次加载数据）`增量同步`（定时任务）

> 4中同步方式  MySQL =》 ES

1. 定时任务：例如一分钟一次，找到时间内变更的数据，进行同步到ES（考虑同步失效后的策略）

优点：占用资源少，简单 ，不用第三方中间件

缺点：有时间差

应用场景：数据短时间内不同步影响不大，数据变更次数少

2. 双写（在写入数据库时，同时写入ES，存在原子性问题，考虑事务）
3. 用Logstash数据同步管道：一般要配合第三方中间件
4. Canal

## SQL预编译的优点

- 灵活
- 简单易懂
- 部分防SQl注入
- 快（有缓存）

## 补充点

1. 关键字高亮

```json
GET post_v1/_search 
{
    "query": {
        "match": {"content" "java" }
    },
    "highlight": { 
        "fields": {
            "content":""
        }
    }
}
```



1. 防抖节流

```js
import debounce from 'lodash/debounce';
import throttle from 'lodash/throttle';

export default {
  data() {
    return {
      // 组件数据
    }
  },
  
  created() {
    // 在 mounted 阶段绑定事件处理函数
    window.addEventListener('scroll', debounce(this.handleScroll, 200));
    window.addEventListener('resize', throttle(this.handleResize, 500));
  },
  
  methods: {
    // 防抖处理函数
    handleScroll: debounce(function() {
      // 处理滚动事件
    }, 200),
    
    // 节流处理函数
    handleResize: throttle(function() {
      // 处理窗口大小改变事件
    }, 500)
  },
  
  destroyed() {
    // 在组件销毁前移除事件监听函数
    window.removeEventListener('scroll', debounce(this.handleScroll, 200));
    window.removeEventListener('resize', throttle(this.handleResize, 500));
  }
}

// 在对应组件调用即可
const debounceMyFunction = debounce(myFunction, 1000);
```

# ginso
