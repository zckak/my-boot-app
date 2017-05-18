# Overview

SpringBootの練習でシャウトボックスを作ってみました。

機能

* 時系列タイムライン
* コメント・画像の投稿
* アカウント作成

# Requirement

* Docker 1.10.0+
* Docker Composer 1.6.0+

# Installation

1). リポジトリをクローン

`$ git clone https://github.com/zckak/my-boot-app.git`

2). Docker環境構築

```
# yum install -y yum-utils
# yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo
# yum-config-manager --enable docker-ce-edge
# yum install docker-ce
```

```
$ curl -L "https://github.com/docker/compose/releases/download/1.11.2/docker-compose-$(uname -s)-$(uname -m)" > docker-compose
$ chmod +x docker-compose
$ sudo mv docker-compose /usr/local/bin/
```

3). 一般ユーザーでdockerコマンドの利用

```
$ sudo gpasswd -a $USER docker
```
再ログイン

# Run

`$ docker-compose up`

http://localhost:8080 
