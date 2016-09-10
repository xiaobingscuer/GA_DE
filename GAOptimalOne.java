
import java.util.Random;
/////////
// author:xiaobingscuer
// topic:GA algorithm one,关于遗传算法（一）
// 这是用 遗传算法 求解一维 最优问题: min f(x),x的范围(-5,5)
// 此程序中，f(x)=(x-2)^2,最优解为 2.000
// 遗传算法采用实数编码，轮盘赌选择，变异为加上小的随机数
// 由于f(x)>0,故个体适应度评估函数为 1/f(x)
// 遗传算法的其他观点：
// 		进化出策略，比如工程上的方案策略，或者机器人的行动策略(可参见书《复杂》）
// 		求解动态规划
/////////
public class GAOptimalOne {
	// 种群规模
	private final static  int POPULATION_SCALE=1000;
	// 进化代数
	private final int EVALUTION_NUM=100;
	// 交叉概率
	//private final float CROSS_PROBABLITY=0.5F;
	// 变异概率
	private final float MUTATE_PROBABLITY=0.1F;
	// x域
	float max=5;
	float min=-5;
		
	public GAOptimalOne() {
		super();
	}
	
	// 生成种群  // 实数编码
	public float[] generatePoupulation(int scale){
		float[] population = new float[scale];
		Random rand=new Random();
		for(int i=0;i<scale;i++){
			population[i]=rand.nextFloat()*10-5;		// 随机生成(-5,5)区间的个体
		}
		return population;
	}
	// 个体适应度评估 
	public double evaluatePerson(float person){
		double fitness=1/Math.pow(person-2,2); 			// 1/f(x),即 1/(x-2)^2
		return fitness;
	}
	// 种群适应度表
	public double[] fitnessList(float[] population){
		double[] fitList=new double[population.length]; // 每个个体的适应度表fitness
		double sum=0;
		for(int i=0;i<population.length;i++){
			fitList[i]=evaluatePerson(population[i]);	// 评估种群population中的个体population[i]的适应度
			sum+=fitList[i];							// 对这些适应度进行累加求和
		}
		for(int i=0;i<population.length;i++){
			fitList[i]=fitList[i]/sum;				    // 求每个适应度所占比例，population[i]/sum，即将适应度转化为概率
		}
		for(int i=0;i<population.length-1;i++){
			fitList[i+1]+=fitList[i];					// 对这些概率求累计概率，代替适应度表，方便在轮盘赌的时候进行选择
		}
		return fitList;
	}
	// 选择 轮盘赌选择
	public float[] choice(float[] population,double[] fitnessList){
		float[] newPopulation=new float[population.length];
		Random rand=new Random();
		for(int i=0;i<population.length;i++){			// 轮盘赌选择，进行种群population.length次选择
			for(int j=0;j<population.length;j++){		// 每次判断选中的是哪个个体
				if(rand.nextFloat()<fitnessList[j]){
					newPopulation[i]=population[j];		// 选出被选中的个体
					break;
				}
			}			
		}
		return newPopulation;
	}
	// 交叉
	public float[] cross(float[] population){			// 因采用实数编码，并且只是一维问题，故没进行交叉操作
		float[] newPopulation=new float[population.length];
		Random rand=new Random();						// 差分进化算法DE中就采用的实数编码，将交叉和变异合并为繁殖
		// 实数编码无交叉
		newPopulation=population;
		return newPopulation;
	}
	// 变异
	public float[] mutate(float[] population){
		float[] newPopulation=new float[population.length];
		Random rand=new Random();
		for(int i=0;i<population.length;i++){
			if(rand.nextFloat()<MUTATE_PROBABLITY){
				population[i]+=0.1*rand.nextFloat();	// 随机变异，即个体加上一个小的随机数
				
			}
		}
		newPopulation=population;
		return newPopulation;
	}
	// 进化
	public float[] evlution(float[] population){
		float[] newPopulation=new float[population.length];
		double[] fitList=new double[population.length];
		newPopulation=population;
		for(int i=0;i<EVALUTION_NUM;i++){
			fitList=fitnessList(newPopulation);			// 适应度评估
			newPopulation=choice(newPopulation,fitList);// 个体选择
			newPopulation=cross(newPopulation);			// 个体间交叉
			newPopulation=mutate(newPopulation);		// 个体变异
		}
		return newPopulation;
	}
	// 从进化相应代数的种群中选出最优个体
	public float[] bestPerson(float[] population){
		float[] bestPer = new float[2];
		double[] fitList=fitnessList(population);
		for(int i=population.length-1;i>0;i--){
			fitList[i]-=fitList[i-1];					// 将累计概率的适应度表转回为概率
		}		
		for(int i=0;i<population.length;i++){
			if(bestPer[0]<fitList[i]){					// 选出适应度值最大的个体
				bestPer[0]=(float) fitList[i];			// bestPer[0]记录最大是适应度值
				bestPer[1]=population[i];				// bestPer[1]记录最优解
			}
		}
		return bestPer;
	}
	
	public static void main(String[] args) {
		
		GAOptimalOne gaOptimal=new GAOptimalOne();			// 定义对象
		float[] bestPer=gaOptimal.bestPerson(gaOptimal.evlution(gaOptimal.generatePoupulation(POPULATION_SCALE))); // 方法调用
		System.out.print("最优个体的适应度："+bestPer[0]+"\n"+"最优解："+bestPer[1]);
	}

}
