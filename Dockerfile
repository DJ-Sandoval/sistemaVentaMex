FROM ubuntu:latest
LABEL authors="Jose"

ENTRYPOINT ["top", "-b"]