startCode(firstOP,operatorList,sinkOpList,sourceOP)::=<<


		ArrayList<IPhysicalOperator> physicalOperatorList = new ArrayList<IPhysicalOperator>();
		
		physicalOperatorList.add($firstOP$PO);
		
		IOperatorOwner operatorOwner = new PhysicalQuery(physicalOperatorList);
		
		
		$operatorList.keys:{k|
 		 	$operatorList.(k)$PO.addOwner(operatorOwner);
  			}
		$


		$sinkOpList : {sinkOP |
			$sinkOP$PO.open(operatorOwner);
		}$

		
	
>>