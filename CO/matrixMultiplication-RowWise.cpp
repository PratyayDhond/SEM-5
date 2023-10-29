#include<iostream>
#include<vector>
using namespace std;

#define vvll vector<vector<long long>> 
// long long matrix1[1024][1024];
// long long matrix2[1024][1024];
// long long result[1024][1024];

// void initMatrix(){
//     for(int i = 0; i < 1024; i++){
//         for(int j = 0; j < 1024; j++){
//             matrix1[i][j] = matrix2[i][j] = result[i][j] = 0;
//         }
//     }
//     return;
// }

void setRandoms(){
    srand(time(0));
}
// 0 cannot be allowed in the matrix as it will cause 0 in multiplication
void randomiseMatrix(vvll &matrix){
    for(int i = 0; i < 1024; i++){
        for(int j = 0; j < 1024; j++){ 
            matrix[i][j] = rand() % 20 + 1;  // to get values from 1 to 9
        }
    }
}

void multiplyMatrix(vvll &matrix1, vvll &matrix2, vvll& result){
    int n = matrix1.size();
    for(int i = 0; i < n; i++){
        for(int j = 0; j < n; j++){
            for(int k = 0; k < n; k++){
                result[i][j] += matrix1[i][k] * matrix2[k][j];
            }
        }
    }

}

// void displayMatrix(vvll matrix){
    
//     for(auto row : matrix){
//         for(auto val : row)
//             cout << val << " ";
//         cout << endl;
//     }
// }

int main(){
    vvll matrix1(1024,vector<long long>(1024));
    vvll matrix2(1024,vector<long long>(1024));
    vvll result(1024,vector<long long>(1024,0));

    setRandoms();
    randomiseMatrix(matrix1);
    randomiseMatrix(matrix2);

    multiplyMatrix(matrix1, matrix2, result);

    // displayMatrix(result);
}




