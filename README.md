#Proyecto de ejemplo de OpenCV en Java con Maven

Bajar OpenCV
http://opencv.org/downloads.html

Generar el jar
http://docs.opencv.org/3.1.0/df/d65/tutorial_table_of_content_introduction.html

Instalar el jar en repositorio local
```
mvn install:install-file -Dfile=opencv-310.jar -DgroupId=org.opencvjar -DartifactId=opencvjar-osx -Dversion=3.1.0 -Dpackaging=jar
```

Comprimir la librería nativa, renombrar a jar e instalar en repositorio local.
```
#Para este ejemplo se trabajó en OSX por lo que se generó el jar con libopencv_java310.dylib
zip opencvjar-runtime-3.1.0-natives-osx-x86_64.zip libopencv_java310.so
mv opencvjar-runtime-3.1.0-natives-osx-x86_64.zip opencvjar-runtime-3.1.0-natives-osx-x86_64.jar
mvn install:install-file -Dfile=opencvjar-runtime-3.1.0-natives-osx-x86_64.jar -DgroupId=org.opencvjar -DartifactId=opencvjar-runtime -Dversion=3.1.0 -Dpackaging=jar -Dclassifier=natives-osx-x86_64
```

Instalar el jar de JavaFX, en el repo local, para que Maven pueda compilar con él
```
mvn install:install-file -Dfile=JAVA_HOME/jre/jfxrt.jar -DgroupId=com.oracle -DartifactId=javafx -Dversion=2.2 -Dpackaging=jar
```

Hecho todo lo anterior, el POM del proyecto debe tener las siguientes dependencias.
```
      <dependency>
          <groupId>com.oracle</groupId>
          <artifactId>javafx</artifactId>
          <version>2.2</version>
      </dependency>

      <dependency>
          <groupId>org.opencvjar</groupId>
          <artifactId>opencvjar-osx</artifactId>
          <version>3.1.0</version>
      </dependency>

      <dependency>
          <groupId>org.opencvjar</groupId>
          <artifactId>opencvjar-runtime</artifactId>
          <version>3.1.0</version>
          <classifier>natives-osx-x86_64</classifier>
      </dependency>
```

Compilar el proyecto
```
mvn clean package
```

Ejecutar
```
java -Djava.library.path=target/natives -jar target/NOMBRE_DEL_ARTEFACTO.jar
```