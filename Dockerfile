FROM ses9892/ec2_shopping_server:tagname

WORKDIR /root/git/shopping/shoppingWeb

RUN git pull
CMD maven package -DshipTests

WORKDIR /root/git/shopping/shoppingWeb/target

ENV PORT 8080
EXPOSE 8080


CMD java -jar application-0.0.1-SNAPSHOT.jar