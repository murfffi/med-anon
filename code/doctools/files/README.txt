See ../diplomna.pdf for details on the software packaging

To test the software:
1. Make sure you have Java 7 (Oracle JDK release from http://www.oracle.com/technetwork/java/javase/downloads/index.html is recommended)
  - environment variable JAVA_HOME must be set
  - JAVA_HOME\bin must be on the path
  - 'java -version' should print (Xs below may be anything depending on your installed version)
java version "1.7.0_XX"
Java(TM) SE Runtime Environment (build 1.7.0 XXXX)
Java HotSpot(TM) 64-Bit Server VM (build XXXXXX, mixed mode)
  
2. Copy samples/sample.txt to a folder where files can be created
3. Go to bin/
4. Run: anon.cmd "path to sample.txt"
5. The anonymized variant of the sample text will be created in the same folder.

UTF-8 Bulgarian description follows

Вижте diplomna.pdf за подробно описание на софтуера.

За да тествате софтуера:
1. Уверете се, че имате Java 7 (препоръчана е Oracle JDK от http://www.oracle.com/technetwork/java/javase/downloads/index.html)
  - environment променливата JAVA_HOME трябва да е дефинарана на директорията в коята Java e инсталирана
  - JAVA_HOME\bin трябва да "бъде на пътя" на операционната система
  - 'java -version' трябва да показа (където знаците X показват индикатори, които са различни според инсталацията)
java version "1.7.0_XX"
Java(TM) SE Runtime Environment (build 1.7.0 XXXX)
Java HotSpot(TM) 64-Bit Server VM (build XXXXXX, mixed mode)

2. Копирайте samples/sample.txt в директория, където могат да се създават изходни файлове
3. Отворете конзолен прозорец в bin/
4. Изпълнете: anon.cmd "път до sample.txt"
5. Изходен файл с анонимизираният текст ще бъде създаден в същата директория като тази на sample.txt