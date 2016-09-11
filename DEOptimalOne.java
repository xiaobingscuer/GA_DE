
import java.util.Random;
/////////
// author:xiaobingscuer
// topic:DE algorithm one,���ڲ�ֽ����㷨��һ��
// ������ ��ֽ����㷨 ���һά ��������: min f(x),x�ķ�Χ(-5,5)
// �˳����У�f(x)=(x-2)^2,���Ž�Ϊ 2.000
// ��ֽ����㷨����ʵ�����룬���̶�ѡ�񣬷�ֳ��ʽΪnewPopulation[i]=population[r1]+(population[r2]-population[r3])
// ����f(x)>0,�ʸ�����Ӧ����������Ϊ 1/f(x)
/////////
public class DEOptimalOne {
	// ��Ⱥ��ģ
	private final static  int POPULATION_SCALE=1000;
	// ��������
	private final int EVALUTION_NUM=100;
	// ��ֳ����
	private final float BREED_PROBABLITY=0.9F;
	// x��
	float max=5;
	float min=-5;
		
	public DEOptimalOne() {
		super();
	}
	
	// ������Ⱥ  // ʵ������
	public float[] generatePoupulation(int scale){
		float[] population = new float[scale];
		Random rand=new Random();
		for(int i=0;i<scale;i++){
			population[i]=rand.nextFloat()*10-5;		// �������(-5,5)����ĸ���
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
		return newPopulation;
	}
	// ��ֳ,���Ŵ��㷨�Ľ�������ͱ�������ϲ�Ϊ��ֳ����
	public float[] breed(float[] population){
		float[] newPopulation=new float[population.length];
		Random rand=new Random();
		int r1,r2,r3;
		for(int i=0;i<population.length;i++){
			if(rand.nextFloat()<BREED_PROBABLITY){		// �ж��Ƿ���Ҫ��ֳ
				r1=rand.nextInt(population.length);
				do{
					r2=rand.nextInt(population.length);
				}while(r2==r1);
				do{
					r3=rand.nextInt(population.length);
				}while(r3==r2||r3==r1);
				newPopulation[i]=population[r1]+(population[r2]-population[r3]); // ��ֳ��ʽ
				if(newPopulation[i]>5){					// �жϸ����Ƿ���x����
					newPopulation[i]=5;
				}if(newPopulation[i]<-5){
					newPopulation[i]=-5;
				}
			}else{
				newPopulation[i]=population[i];
			}
		}
		return newPopulation;
	}
	// ����
	public float[] evlution(float[] population){
		float[] newPopulation;
		double[] fitList;
		newPopulation=population;
		for(int i=0;i<EVALUTION_NUM;i++){
			fitList=fitnessList(newPopulation);			// ��Ӧ������
			newPopulation=choice(newPopulation,fitList);// ����ѡ��
			newPopulation=breed(newPopulation);			// ��ֳ
		}
		return newPopulation;
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
		DEOptimalOne deOptimal=new DEOptimalOne();			// �������
		float[] bestPer=deOptimal.bestPerson(deOptimal.evlution(deOptimal.generatePoupulation(POPULATION_SCALE))); // ��������
		long endTime=System.currentTimeMillis();			// ����ʱ��	
		System.out.print("���Ÿ������Ӧ�ȣ�"+bestPer[0]+"\n"+"���Ž⣺"+bestPer[1]+"\n"+"��ʱ(ms):"+(endTime-startTime));
		
	}

}
