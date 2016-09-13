
import java.util.Random;
/////////
// author:xiaobingscuer
// topic:DE algorithm one,���ڲ�ֽ����㷨��һ��
// ������ ��ֽ����㷨 ���һά ��������: min f(x),x�ķ�Χ(-5,5)
// �˳����У�f(x)=(x-2)^2,���Ž�Ϊ 2.000
// ��ֽ����㷨����ʵ�����룬���̶�ѡ�񣬷�ֳ��ʽΪnewPopulation[i]=population[r1]+CROSS_FACTOR*(population[r2]-population[r3])
// ����f(x)>0,�ʸ�����Ӧ����������Ϊ 1/f(x)
// ���������Ŵ��㷨�Ľ�����졢���ȶ����������㾫��Ҫ��
/////////
public class DEOptimalOne {
	// ��Ⱥ��ģ
	private final static  int POPULATION_SCALE=1000;
	// ��������
	private final int EVALUTION_NUM=100;
	// �������
	private final float CROSS_PROBABLITY=0.1F;
	// ��������
	private final float CROSS_FACTOR=0.5F;
	// x��
	private final float MAX=5F;
	private final float MIN=-5F;
		
	public DEOptimalOne() {
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
		return 1/Math.pow(person-2,2); 			// 1/f(x),�� 1/(x-2)^2
	}
	// ��Ⱥ��Ӧ�ȱ�
	public double[] fitnessList(float[] population){
		double[] fitList=new double[population.length]; // ÿ���������Ӧ�ȱ�fitness
		for(int i=0;i<population.length;i++){
			fitList[i]=evaluatePerson(population[i]);	// ������Ⱥpopulation�еĸ���population[i]����Ӧ��
		}
		return fitList;
	}
	// ѡ�� //��֮ǰ����Ӧ�ȱȽϣ���Ӧ�ȱ���ǰ��ͱ����µĸ��壬��������ǰ�ĸ���
	public float[] choice(float[] population,double[] fitnessList,float[] newPopulation,double[] newFitnessList){
		for(int i=0;i<population.length;i++){
			if(newFitnessList[i]<fitnessList[i]){
				newPopulation[i]=population[i];
			}
		}
		return newPopulation;
	}
	// ��ֳ,���Ŵ��㷨�Ľ�������ͱ�������ϲ�Ϊ��ֳ����
	public float[] breed(float[] population){
		float[] newPopulation =new float[population.length];
		Random rand=new Random();
		int r1,r2,r3;
		for(int i=0;i<population.length;i++){
			if(rand.nextFloat()<CROSS_PROBABLITY){		// �ж��Ƿ���Ҫ��ֳ
				r1=rand.nextInt(population.length);		// �������3��������ͬ�ĸ���
				do{
					r2=rand.nextInt(population.length);
				}while(r2==r1);
				do{
					r3=rand.nextInt(population.length);
				}while(r3==r2||r3==r1);
				newPopulation[i]=population[r1]+CROSS_FACTOR*(population[r2]-population[r3]); // ��ֳ��ʽ
//				if(newPopulation[i]>MAX){					// �жϸ����Ƿ���x����
//					newPopulation[i]=MAX;
//				}if(newPopulation[i]<MIN){
//					newPopulation[i]=MIN;
//				}
			}else{
				newPopulation[i]=population[i];
			}
		}
		return newPopulation;
	}
	// ����
	public float[] evlution(float[] population){
		float[] newPopulation = null;
		double[] fitList;
		double[] newFitList;
		for(int i=0;i<EVALUTION_NUM;i++){
			fitList=fitnessList(population);			// ��һ����Ӧ������
			newPopulation=breed(population);			// ��ֳ
			newFitList=fitnessList(newPopulation);		// ��һ����Ӧ������
			newPopulation=choice(population,fitList,newPopulation,newFitList);// ����ѡ��
			population=newPopulation;
			float[] best=bestPerson(population);
			if(Math.abs(best[1]-2.0)<0.001){			// ���� ����Ҫ�� �����ǰ�˳�
				break;
			}			
		}
		return newPopulation;
	}
	// �ӽ�����Ӧ��������Ⱥ��ѡ�����Ÿ���
	public float[] bestPerson(float[] population){
		float[] bestPer = new float[2];
		double[] fitList=fitnessList(population);
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
		DEOptimalOne deOptimal=new DEOptimalOne();			// �������
		float[] bestPer=deOptimal.bestPerson(deOptimal.evlution(deOptimal.generatePoupulation(POPULATION_SCALE))); // ��������
		long endTime=System.currentTimeMillis();			// ����ʱ��	
		System.out.print("���Ÿ������Ӧ�ȣ�"+bestPer[0]+"\n"+"���Ž⣺"+bestPer[1]+"\n"+"��ʱ(ms):"+(endTime-startTime));
		
	}

}
