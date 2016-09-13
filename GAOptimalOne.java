
import java.util.Random;
/////////
// author:xiaobingscuer
// topic:GA algorithm one,�����Ŵ��㷨��һ��
// ������ �Ŵ��㷨 ���һά ��������: min f(x),x�ķ�Χ(-5,5)
// �˳����У�f(x)=(x-2)^2,���Ž�Ϊ 2.000
// ���뷽���� �����Ʊ��롢ʵ�����롢���ű���
// ���淽ʽ��
//	  ���ڶ����Ʊ��룺���㽻�棬��㽻�桢���Ƚ���
//	 ����ʵ�����룺�������桢����ʽ����
// ���췽ʽ��
//	 ���ڶ����Ʊ��룺���ȱ���
//	 ����ʵ�����룺�����ϸ���С�ĸ�˹����Ŷ��ľ��ȱ��졢�Ǿ��ȱ���
// ��Ӧ������������f(x)>0,�ʸ�����Ӧ����������Ϊ 1/f(x)
// �˳��򷽰���
//		ʵ�����롢��������/����ʽ���桢���ȱ���
// �ӳ�����������������Ʊ��뷽���Աȣ�ʵ�����뷽������򵥿��١�������ȶ���ȷ
// �Ŵ��㷨�������۵㣺
// 		���������ԣ����繤���ϵķ������ԣ����߻����˵��ж�����(�ɲμ��顶���ӡ���
// 		��⶯̬�滮
/////////
public class GAOptimalOne {
	// ��Ⱥ��ģ
	private final static  int POPULATION_SCALE=1000;
	// ��������
	private final int EVALUTION_NUM=100;
	// ��������
	private final float CROSS_ALPHA=1F;
	// �������
	private final float MUTATE_PROBABLITY=0.1F;
	// x��
	private final float MAX=5F;
	private final float MIN=-5F;
		
	public GAOptimalOne() {
		super();
	}
	
	// ������Ⱥ  // ʵ������
	public float[] generatePoupulation(int scale){
		float[] population = new float[scale];
		Random rand=new Random();
		for(int i=0;i<scale;i++){
			population[i]=rand.nextFloat()*(MAX-MIN)+MIN;		// �������(-5,5)����ĸ���
		}
		return population;
	}
	// ������Ӧ������ 
	public double evaluatePerson(float person){
		double fitness=1/Math.pow(person-2,2); 			// 1/f(x),�� 1/(x-2)^2
		return fitness;
	}
	// ��Ⱥ��Ӧ�ȱ�
	public double[] fitnessList(float[] population){
		double[] fitList=new double[population.length]; // ÿ���������Ӧ�ȱ�fitness
		double sum=0;
		for(int i=0;i<population.length;i++){
			fitList[i]=evaluatePerson(population[i]);	// ������Ⱥpopulation�еĸ���population[i]����Ӧ��
			sum+=fitList[i];							// ����Щ��Ӧ�Ƚ����ۼ����
		}
		for(int i=0;i<population.length;i++){
			fitList[i]=fitList[i]/sum;				    // ��ÿ����Ӧ����ռ������population[i]/sum��������Ӧ��ת��Ϊ����
		}
		for(int i=0;i<population.length-1;i++){
			fitList[i+1]+=fitList[i];					// ����Щ�������ۼƸ��ʣ�������Ӧ�ȱ����������̶ĵ�ʱ�����ѡ��
		}
		return fitList;
	}
	// ѡ�� ���̶�ѡ��
	public float[] choice(float[] population,double[] fitnessList){
		float[] newPopulation=new float[population.length];
		Random rand=new Random();
		for(int i=0;i<population.length;i++){			// ���̶�ѡ�񣬽�����Ⱥpopulation.length��ѡ��
			for(int j=0;j<population.length;j++){		// ÿ���ж�ѡ�е����ĸ�����
				if(rand.nextFloat()<fitnessList[j]){
					newPopulation[i]=population[j];		// ѡ����ѡ�еĸ���
					break;
				}
			}			
		}
		population=null;
		return newPopulation;
	}
	// ����
	public float[] cross(float[] population){			// �����ʵ�����룬����ֻ��һά���⣬��û���н������
		float[] newPopulation=new float[population.length];
		Random rand=new Random();						// ��ֽ����㷨DE�оͲ��õ�ʵ�����룬������ͱ���ϲ�Ϊ��ֳ
		for(int i=0;i<population.length;i++){
			int personOne=rand.nextInt(population.length);  // ��ѡ������ͬ�ĸ��� 
			int personTwo=0;
			do{
				personTwo=rand.nextInt(population.length);
			}while(personOne==personTwo);
			// ���������������
			newPopulation[i]=CROSS_ALPHA*population[personOne]+(1-CROSS_ALPHA)*population[personTwo];// ��������	
			// �����������ʽ����
//			double fitOne=evaluatePerson(population[personOne]);
//			double fitTwo=evaluatePerson(population[personTwo]);	
//			if(fitOne<fitTwo){
//				newPopulation[i]=population[personTwo]+CROSS_ALPHA*(population[personTwo]-population[personOne]);
//			}else{
//				newPopulation[i]=population[personOne]+CROSS_ALPHA*(population[personOne]-population[personTwo]);
//			}
		}
		population=null;
		return newPopulation;
	}
	// ����
	public float[] mutate(float[] population){
		Random rand=new Random();
		int num=(int)MUTATE_PROBABLITY*POPULATION_SCALE;
		int position=0;
		for(int i=0;i<num;i++){
			position=rand.nextInt(population.length);		
			population[position]+=0.1*rand.nextFloat();	 // ������죬�������ĳ������Ϊȡ���0/1
			if(population[position]>MAX){					// �жϸ����Ƿ���x����
				population[position]=MAX;
			}if(population[position]<-MIN){
				population[position]=MIN;
			}
		}
		
		return population;
	}
	// ����
	public float[] evlution(float[] population){
		double[] fitList;
		for(int i=0;i<EVALUTION_NUM;i++){
			fitList=fitnessList(population);		// ��Ӧ������
			population=choice(population,fitList);	// ����ѡ��
			population=cross(population);			// ����佻��
			population=mutate(population);			// �������
			float[] best=bestPerson(population);
			if(Math.abs(best[1]-2.0)<0.001){		// ���� ����Ҫ�� �����ǰ�˳�
				break;
			}
		}
		return population;
	}
	// �ӽ�����Ӧ��������Ⱥ��ѡ�����Ÿ���
	public float[] bestPerson(float[] population){
		float[] bestPer = new float[2];
		double[] fitList=fitnessList(population);
		for(int i=population.length-1;i>0;i--){
			fitList[i]-=fitList[i-1];					// ���ۼƸ��ʵ���Ӧ�ȱ�ת��Ϊ����
		}		
		for(int i=0;i<population.length;i++){
			if(bestPer[0]<fitList[i]){					// ѡ����Ӧ��ֵ���ĸ���
				bestPer[0]=(float) fitList[i];			// bestPer[0]��¼�������Ӧ��ֵ
				bestPer[1]=population[i];				// bestPer[1]��¼���Ž�
			}
		}
		return bestPer;
	}
	
	public static void main(String[] args) {

		long startTime=System.currentTimeMillis();			// ��ʼʱ��
		GAOptimalOne gaOptimal=new GAOptimalOne();			// �������
		float[] bestPer=gaOptimal.bestPerson(gaOptimal.evlution(gaOptimal.generatePoupulation(POPULATION_SCALE))); // �������� // ����// ѡ�����Ÿ���	
		long endTime=System.currentTimeMillis();			// ����ʱ��		
		System.out.print("���Ÿ������Ӧ�ȣ�"+bestPer[0]+"\n"+"���Ž⣺"+bestPer[1]+"\n"+"��ʱ(ms):"+(endTime-startTime));
	}

}
