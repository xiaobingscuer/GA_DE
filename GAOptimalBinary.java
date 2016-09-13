
import java.util.Random;
/////////
//author:xiaobingscuer
//topic:GA algorithm two,关于遗传算法（二）
//这是用 遗传算法 求解一维 最优问题: min f(x),x的范围(-5,5)
//此程序中，f(x)=(x-2)^2,最优解为 2.000
//编码方案： 二进制编码、实数编码、符号编码
//交叉方式：
//	对于二进制编码：单点交叉，多点交叉、均匀交叉
//	对于实数编码：算术交叉、启发式交叉
//变异方式：
//	对于二进制编码：均匀变异
//	对于实数编码：个体上附加小的高斯随机扰动的均匀变异、非均匀变异
//适应度评估：由于f(x)>0,故个体适应度评估函数为 1/f(x)
//此程序方案：
//		二进制编码、单点交叉、均匀变异
//遗传算法的其他观点：
//	进化出策略，比如工程上的方案策略，或者机器人的行动策略(可参见书《复杂》）
//	求解动态规划
/////////
public class GAOptimalBinary {
	// 种群规模
	private final static  int POPULATION_SCALE=1000;
	// 进化代数
	private final int EVALUTION_NUM=100;
	// 交叉概率
	private final float CROSS_PROBABLITY=1F;
	// 变异概率
	private final float MUTATE_PROBABLITY=0.1F;
	// 编码长度
	private final static int ENCODE_LENGTH=7;
	// x域
	private final float MAX=5F;
	private final float MIN=-5F;
	// 随机数
	
	
	public GAOptimalBinary() {
		super();
	}
	
	// 生成种群  // 实数编码
	public int[][] generatePoupulation(int scale,int edcodeLength){
		int[][] population = new int[scale][edcodeLength];	
		Random rand=new Random();
		for(int i=0;i<scale;i++){
			for(int j=0;j<edcodeLength;j++){
				population[i][j]=rand.nextInt(2);		// 随机生成（a6,a5,a4,a3,a2,a1,a0)的个体
			}			
		}
		return population;
	}
	// 个体解码
	public float decode(int[] person){
		int sum=0;
		for(int i=0;i<person.length;i++){
			sum+=Math.pow(2, person.length-1-i)*person[i];
		}
		float valueOfPerson=MIN+sum*(MAX-MIN)/((int)Math.pow(2, person.length)-1);
		return valueOfPerson;
	}
	// 个体适应度评估 
	public double evaluatePerson(int[] person){
		float valueOfPerson=decode(person);				// 个体解码
		double fitness=1/Math.pow(valueOfPerson-2,2); 	// 1/f(x),即 1/(x-2)^2
		return fitness;
	}
	// 种群适应度表
	public double[] fitnessList(int[][] population){
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
	public int[][] choice(int[][] population,double[] fitnessList){
		int[][] newPopulation=new int[population.length][population[0].length];
		Random rand=new Random();
		for(int i=0;i<population.length;i++){			// 轮盘赌选择，进行种群population.length次选择
			for(int j=0;j<population.length;j++){		// 每次判断选中的是哪个个体
				if(rand.nextFloat()<fitnessList[j]){
					newPopulation[i]=population[j];		// 选出被选中的个体
					break;
				}
			}			
		}
		population=null;
		return newPopulation;
	}
	// 交叉
	public int[][] cross(int[][] population){			
		int[][] newPopulation=new int[population.length][population[0].length];
		Random rand=new Random();
		int where=5;										// 单点交叉	// 选择从哪个基因
		for(int i=0;i<population.length;i+=2){
			if(rand.nextFloat()<CROSS_PROBABLITY){			// 执行单点交叉	
				int perPerson=population[i][where];
				population[i][where]=population[i+1][where];
				population[i+1][where]=perPerson;				
			}				
		}
		return population;
	}
	// 变异
	public int[][] mutate(int[][] population){
		Random rand=new Random();	
		int num=(int)MUTATE_PROBABLITY*POPULATION_SCALE;
		int position=0;
		int mark=0;
		for(int i=0;i<num;i++){
			position=rand.nextInt(population.length);						// position可能会有重复
			mark=rand.nextInt(population[0].length);
			for(int j=0;j<population[0].length;j++){
				if(rand.nextFloat()<0.1){
					population[position][mark]=1-population[position][mark]; // 随机变异，即个体的某个基因为取随机0/1
				}
			} 
		}	
		return population;
	}
	// 进化
	public int[][] evlution(int[][] population){
		double[] fitList;
		for(int i=0;i<EVALUTION_NUM;i++){
			fitList=fitnessList(population);			// 适应度评估
			population=choice(population,fitList);		// 个体选择
			population=cross(population);				// 个体间交叉
			population=mutate(population);				// 个体变异
			float[] best=bestPerson(population);
			if(Math.abs(best[2]-2.0)<0.001){			// 满足 精度要求 后可提前退出
				break;
			}
		}
		return population;
	}
	// 从进化相应代数的种群中选出最优个体
	public float[] bestPerson(int[][] population){
		float[] bestPer = new float[3];
		double[] fitList=fitnessList(population);
		for(int i=population.length-1;i>0;i--){
			fitList[i]-=fitList[i-1];					// 将累计概率的适应度表转回为概率
		}
		int mark=0;
		for(int i=0;i<population.length;i++){
			if(bestPer[0]<fitList[i]){		
				mark=i;								// 选出适应度值最大的个体
			}
		}
		bestPer[0]=(float) fitList[mark];			// bestPer[0]记录最大是适应度值
		String encode="";
		for(int i=0;i<population[mark].length;i++){
			encode=encode+population[mark][i];
		}
		bestPer[1]=Integer.parseInt(encode);	// bestPer[1]记录最优个体的编码
		bestPer[2]=decode(population[mark]);	// bestPer[2]记录最优个体的解码
		return bestPer;
	}
	
	public static void main(String[] args) {

		long startTime=System.currentTimeMillis();			// 开始时间
		GAOptimalBinary gaOptimal=new GAOptimalBinary();	// 定义对象

		float[] bestPer=gaOptimal.bestPerson(gaOptimal.evlution(gaOptimal.generatePoupulation(POPULATION_SCALE,ENCODE_LENGTH))); // 方法调用 // 进化// 选出最优个体	
		long endTime=System.currentTimeMillis();			// 结束时间		
		System.out.print("最优个体的适应度："+bestPer[0]+"\n"+"最优个体的编码："+bestPer[1]+"\n"+"最优解："+bestPer[2]+"\n"+"用时(ms):"+(endTime-startTime));
	}

}
