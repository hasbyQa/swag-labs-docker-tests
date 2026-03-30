# Maven + Java 17 base — handles compile and test in one image
FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app

# Install Chrome stable + all its dependencies in one layer
RUN apt-get update && apt-get install -y \
    wget gnupg curl unzip \
    fonts-liberation libatk-bridge2.0-0 libatk1.0-0 \
    libcups2 libdbus-1-3 libgdk-pixbuf2.0-0 libnspr4 \
    libnss3 libx11-xcb1 libxcomposite1 libxdamage1 \
    libxrandr2 xdg-utils libgbm1 libxkbcommon0 libasound2 \
    && wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" \
       > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Verify Chrome installed correctly — fails build early if something's wrong
RUN google-chrome --version

# Copy pom.xml first so dependency downloads are cached as their own layer
# If pom.xml hasn't changed, Docker skips this on the next build
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

# SUREFIRE_TIMEOUT gives each test more time in the slower CI/Docker environment
# forkedProcessTimeoutInSeconds=300 gives the full suite 5 minutes max per fork
ENV JAVA_OPTS="-Xmx512m"

CMD ["mvn", "test", "-B", \
     "-Dsurefire.failIfNoSpecifiedTests=false", \
     "-Dmaven.surefire.timeout=300"]