# 基础镜像
FROM openjdk:17
# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 拷贝 jar 包
COPY code-challenge-0.0.1.jar /code-challenge-0.0.1.jar
# 暴露端口
EXPOSE 8080
# 入口
ENTRYPOINT ["java","-jar","/code-challenge-0.0.1.jar","--spring.profiles.active=prod"]