registryHandler(clazzName,clazzSimpleName,handlerList, registerFunctionName)::=<<

$clazzName$  $clazzSimpleName$ = new $clazzName$();
$handlerList : {handler |
$clazzSimpleName$.$registerFunctionName$(new $handler$());
}$


>>