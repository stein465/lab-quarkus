#!/bin/bash -xe
APP=$1
GREEN_CONTAINER_DEPLOY_TAG=$2

# Verificar se os argumentos foram fornecidos
if [ $# -lt 2 ]; then
  echo "Usage: $0 <app_name> <deploy_tag>"
  exit 1
fi

# Obter os containers azuis (versão antiga)
BLUE_CONTAINERS=$(docker ps -qf "name=${APP}")
BLUE_CONTAINERS_SCALE=$(echo "$BLUE_CONTAINERS" | wc -w | xargs)

# Definir a escala do novo container verde (caso não haja containers antigos, a escala será 1)
GREEN_CONTAINERS_SCALE=$((BLUE_CONTAINERS_SCALE * 1))  # ou deixar 1 container por vez
if [[ $BLUE_CONTAINERS_SCALE == 0 ]]; then
    GREEN_CONTAINERS_SCALE=1
fi

# Iniciar a nova versão (verde) com a nova tag
TAG=$GREEN_CONTAINER_DEPLOY_TAG docker compose up -d "$APP" --scale "$APP=$GREEN_CONTAINERS_SCALE" --no-build

# Verificar se os novos containers verdes estão saudáveis
until [ "$(docker ps -q -f "name=${APP}" -f "health=healthy" | wc -l | xargs)" == "$GREEN_CONTAINERS_SCALE" ]; do
    sleep 1;  # Intervalo de 1 segundo
done;

# Se houver containers azuis, parar eles depois que o verde estiver saudável
if [[ $BLUE_CONTAINERS_SCALE -gt 0 ]]; then
    docker stop "$BLUE_CONTAINERS"  # Use stop ao invés de kill para permitir shutdown adequado
    docker rm "$BLUE_CONTAINERS"    # Remover os containers antigos
fi
