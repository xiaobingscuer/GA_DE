
import java.util.Random;
/////////
//author:xiaobingscuer
//topic:GA algorithm two,�����Ŵ��㷨������
//������ �Ŵ��㷨 ���һά ��������: min f(x),x�ķ�Χ(-5,5)
//�˳����У�f(x)=(x-2)^2,���Ž�Ϊ 2.000
//���뷽���� �����Ʊ��롢ʵ�����롢���ű���
//���淽ʽ��
//	���ڶ����Ʊ��룺���㽻�棬��㽻�桢���Ƚ���
//	����ʵ�����룺�������桢����ʽ����
//���췽ʽ��
//	���ڶ����Ʊ��룺���ȱ���
//	����ʵ�����룺�����ϸ���С�ĸ�˹����Ŷ��ľ��ȱ��졢�Ǿ��ȱ���
//��Ӧ������������f(x)>0,�ʸ�����Ӧ����������Ϊ 1/f(x)
//�˳��򷽰���
//		�����Ʊ��롢���㽻�桢���ȱ���
//�Ŵ��㷨�������۵㣺
//	���������ԣ����繤���ϵķ������ԣ����߻����˵��ж�����(�ɲμ��顶���ӡ���
//	��⶯̬�滮
/////////
public class GAOptimalBinary {
	// ��Ⱥ��ģ
	private final static  int POPULATION_SCALE=1000;
	// ��������
	private final int EVALUTION_NUM=100;
	// �������
	private final float CROSS_PROBABLITY=1F;
	// �������
	private final float MUTATE_PROBABLITY=0.1F;
	// ���볤��
	private final static int ENCODE_LENGTH=7;
	// x��
	private final float MAX=5F;
	private final float MIN=-5F;
	// �����
	
	
	public GAOptimalBinary() {
		super();
	}
	
	// ������Ⱥ  // ʵ������
	public int[][] generatePoupulation(int scale,int edcodeLength){
		int[][] population = new int[scale][edcodeLength];	
		Random rand=new Random();
		for(int i=0;i<scale;i++){
			for(int j=0;j<edcodeLength;j++){
				population[i][j]=rand.nextInt(2);		// ������ɣ�a6,a5,a4,a3,a2,a1,a0)�ĸ���
			}			
		}
		return population;
	}
	// �������
	public float decode(int[] person){
		int sum=0;
		for(int i=0;i<person.length;i++){
			sum+=Math.pow(2, person.length-1-i)*person[i];
		}
		float valueOfPerson=MIN+sum*(MAX-MIN)/((int)Math.pow(2, person.length)-1);
		return valueOfPerson;
	}
	// ������Ӧ������ 
	public double evaluatePerson(int[] person){
		float valueOfPerson=decode(person);				// �������
		double fitness=1/Math.pow(valueOfPerson-2,2); 	// 1/f(x),�� 1/(x-2)^2
		return fitness;
	}
	// ��Ⱥ��Ӧ�ȱ�
	public double[] fitnessList(int[][] population){
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
	public int[][] choice(int[][] population,double[] fitnessList){
		int[][] newPopulation=new int[population.length][population[0].length];
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
	public int[][] cross(int[][] population){			
		int[][] newPopulation=new int[population.length][population[0].length];
		Random rand=new Random();
		int where=5;										// ���㽻��	// ѡ����ĸ�����
		for(int i=0;i<population.length;i+=2){
			if(rand.nextFloat()<CROSS_PROBABLITY){			// ִ�е��㽻��	
				int perPerson=population[i][where];
				population[i][where]=population[i+1][where];
				population[i+1][where]=perPerson;				
			}				
		}
		return population;
	}
	// ����
	public int[][] mutate(int[][] population){
		Random rand=new Random();	
		int num=(int)MUTATE_PROBABLITY*POPULATION_SCALE;
		int position=0;
		int mark=0;
		for(int i=0;i<num;i++){
			position=rand.nextInt(population.length);						// position���ܻ����ظ�
			mark=rand.nextInt(population[0].length);
			for(int j=0;j<population[0].length;j++){
				if(rand.nextFloat()<0.1){
					population[position][mark]=1-population[position][mark]; // ������죬�������ĳ������Ϊȡ���0/1
				}
			} 
		}	
		return population;
	}
	// ����
	public int[][] evlution(int[][] population){
		double[] fitList;
		for(int i=0;i<EVALUTION_NUM;i++){
			fitList=fitnessList(population);			// ��Ӧ������
			population=choice(population,fitList);		// ����ѡ��
			population=cross(population);				// ����佻��
			population=mutate(population);				// �������
			float[] best=bestPerson(population);
			if(Math.abs(best[2]-2.0)<0.001){			// ���� ����Ҫ�� �����ǰ�˳�
				break;
			}
		}
		return population;
	}
	// �ӽ�����Ӧ��������Ⱥ��ѡ�����Ÿ���
	public float[] bestPerson(int[][] population){
		float[] bestPer = new float[3];
		double[] fitList=fitnessList(population);
		for(int i=population.length-1;i>0;i--){
			fitList[i]-=fitList[i-1];					// ���ۼƸ��ʵ���Ӧ�ȱ�ת��Ϊ����
		}
		int mark=0;
		for(int i=0;i<population.length;i++){
			if(bestPer[0]<fitList[i]){		
				mark=i;								// ѡ����Ӧ��ֵ���ĸ���
			}
		}
		bestPer[0]=(float) fitList[mark];			// bestPer[0]��¼�������Ӧ��ֵ
		String encode="";
		for(int i=0;i<population[mark].length;i++){
			encode=encode+population[mark][i];
		}
		bestPer[1]=Integer.parseInt(encode);	// bestPer[1]��¼���Ÿ���ı���
		bestPer[2]=decode(population[mark]);	// bestPer[2]��¼���Ÿ���Ľ���
		return bestPer;
	}
	
	public static void main(String[] args) {

		long startTime=System.currentTimeMillis();			// ��ʼʱ��
		GAOptimalBinary gaOptimal=new GAOptimalBinary();	// �������

		float[] bestPer=gaOptimal.bestPerson(gaOptimal.evlution(gaOptimal.generatePoupulation(POPULATION_SCALE,ENCODE_LENGTH))); // �������� // ����// ѡ�����Ÿ���	
		long endTime=System.currentTimeMillis();			// ����ʱ��		
		System.out.print("���Ÿ������Ӧ�ȣ�"+bestPer[0]+"\n"+"���Ÿ���ı��룺"+bestPer[1]+"\n"+"���Ž⣺"+bestPer[2]+"\n"+"��ʱ(ms):"+(endTime-startTime));
	}

}
