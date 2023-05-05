# How to upgrade the Gradle Wrapper version

Visit the [Gradle website](https://gradle.org/releases) and decide the:

- desired version
- desired distribution type
- what is the sha256 for the version and type chosen above

Adjust the following command with tha arguments above and execute it twice:

```
./gradlew wrapper --gradle-version 8.1.1 \
    --distribution-type bin \
    --gradle-distribution-sha256-sum e111cb9948407e26351227dabce49822fb88c37ee72f1d1582a69c68af2e702f
```

---

The first execution should automatically update:

- `doodle/gradle/wrapper/gradle-wrapper.properties`

---

The second execution should then update:

- `demos-0-9-1/gradle/wrapper/gradle-wrapper.jar`
- `demos-0-9-1/gradlew`
- `demos-0-9-1/gradlew.bat`

---

Verify the upgraded `gradle/wrapper/gradle-wrapper.jar` checksum:

```
export WRAPPER_JAR="gradle/wrapper/gradle-wrapper.jar"
export CHECKSUM="ed2c26eba7cfb93cc2b7785d05e534f07b5b48b5e7fc941921cd098628abca58"
echo "$CHECKSUM $WRAPPER_JAR" | sha256sum -c
```

---

The four updated files are ready to be committed.
