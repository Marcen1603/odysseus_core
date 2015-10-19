timeValueItem(timeValueItem)::=<<
	$if(timeValueItem)$
new TimeValueItem($timeValueItem.time$,TimeUnit.valueOf("$timeValueItem.unit$"))
		$else$
null
	$endif$
>>