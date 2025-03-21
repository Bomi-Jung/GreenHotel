package com.smhrd.hotelreservation.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 어노테이션이 적용될 수 있는 위치
@Retention(RetentionPolicy.RUNTIME) // 런타임 동안 유지
public @interface Login {}