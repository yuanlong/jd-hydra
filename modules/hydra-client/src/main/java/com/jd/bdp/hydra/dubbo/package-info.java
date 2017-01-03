
/**
 * hydra监控，生成的traceid是独立的，无法与java web系统的traceId进行串起来，原因
 * hydra主要针对dubbo rpc 调用内容监控，不包含web入口的请求追踪。
 * 但我们一般情况都有遇到需要根据将入口的web请求与后台的rpc调用整个的调用链串起来，
 * 但是这两个traceid不能衔接是不行的，增加
 * MDCHydraFilter类替换hydarFilter来完成这个事情，
 * 在web请求产生的traceid通过log4j的mdc来进行跟踪,
 * rpc调用能过filter进行跟踪;
 * 
 */
package com.jd.bdp.hydra.dubbo;