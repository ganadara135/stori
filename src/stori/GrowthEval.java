package stori;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.collections.IndexedIterable;

public class GrowthEval {

	int checkInterval;
	
	
	public GrowthEval() {		
		checkInterval = 0;			
	}
	
	// 7 ticks �������� ���������� �� ����
	@ScheduledMethod(start = 1, interval = 7)
	public void stepGrowth() {

		checkInterval++;
		//System.out.println("checkInterval : " + checkInterval);
		
		Context<Object> contextForEval = ContextUtils.getContext(this);
		//System.out.println(contextForEval.getObjects(StoriBoard.class));
		System.out.println("Storiboard Count : " + contextForEval.getObjects(StoriBoard.class).size());
		
		IndexedIterable<Object> storiIter  = contextForEval.getObjects(StoriBoard.class);
		
		System.out.println("���� Tick : " + RepastEssentials.GetTickCount());
		
		
	
		// �ӽ÷� ���⼭ ���丮�鿡  GrowthIndex ������ �������� �ְ�(��ü ���丮�� 10%),  �ٷ� ������
		StoriBoard storiTmp = null;
		for(int x = 0; x < storiIter.size() * 0.1; x++) {
			storiTmp = (StoriBoard) storiIter.get(RandomHelper.nextIntFromTo(0, storiIter.size()-1));
			storiTmp.growthIndex = RandomHelper.nextIntFromTo(1, 7);  // �ִ� 1������ 7�� ������ ������ �Ҵ�
		}
		
		// ���⼭ 2���� ó�� List �� ����� �Ͱ� Mint ó�� �ϴ� �κ�
		// Mint ó�� �ϴ� �κ�, ���� �� ���� Staking ������ ���� ���÷��̼� ó��
		List<StoriBoard> storiBoardListTmp = new ArrayList<>();	
	//	int index = 0;
		int tmpVal = 0;
		for(Object obj : storiIter) {
			// ������ ���� List ����� �κ�
			storiBoardListTmp.add((StoriBoard) obj);
			// Mint ó�� �κ�
			tmpVal = ((StoriBoard)obj).pi.stakingThisWeek;
			if(tmpVal > 0){
				MyContextBuilder.LiquidToken += tmpVal;
				((StoriBoard)obj).pi.totalStaking += tmpVal;
				((StoriBoard)obj).pi.stakingThisWeek = 0;
			}
			//index++;
		}
		// �ٷ� �ִ� �� ���� (Staking �ݾװ� growthIndex �� ���� ���� ���丮�� ������ ���� ���� �ű�
		Collections.sort(storiBoardListTmp, new CompareGrowthIndex());
		

		for (int z=0 ; z < storiBoardListTmp.size() * 0.2; z++) {
			storiTmp =  storiBoardListTmp.get(z);
			storiTmp.pi.totalStaking += 1;
			// 20% �� �ȿ� �ȵ�����, growthIndex�� �ִ� ���丮�� ������ ���򰡽� �ݿ���
			storiTmp.growthIndex = 0; 
		}
		
//		for (Object orderedList : storiBoardListTmp) {
//            System.out.println((((StoriBoard)orderedList).growthIndex));
//        }
		
	}
	
	
	// ��������(Desc) ����
	static class CompareGrowthIndex implements Comparator<StoriBoard>{
		 
        @Override
        public int compare(StoriBoard o1, StoriBoard o2) {
        	return o1.growthIndex + o1.pi.totalStaking > o2.growthIndex + o2.pi.totalStaking 
        		? -1 : o1.growthIndex + o1.pi.totalStaking < o2.growthIndex + o2.pi.totalStaking
        		? 1 : 0;
        }
    }

}
