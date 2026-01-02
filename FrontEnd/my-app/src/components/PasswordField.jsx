import { Eye, EyeOff, Lock, AlertCircle } from "lucide-react";

const PasswordField = ({
  label,
  name,
  value,
  showPassword,
  setShowPassword,
  onChange,
  onBlur,
  onKeyPress,
  error,
  touched,
  autoComplete
}) => {
  return (
    <div className="space-y-2">
      <label className="block text-sm font-medium text-gray-700">
        {label}
      </label>

      <div className="relative">
        <div className="absolute inset-y-0 left-0 pl-3 flex items-center">
          <Lock className="h-5 w-5 text-gray-400" />
        </div>

        <input
          type={showPassword ? "text" : "password"}
          name={name}
          value={value}
          onChange={onChange}
          onBlur={onBlur}
          onKeyPress={onKeyPress}
          autoComplete={autoComplete}
          className={`block w-full pl-10 pr-10 py-2.5 border ${
            touched && error
              ? "border-red-300 focus:ring-red-500"
              : "border-gray-300 focus:ring-blue-500"
          } rounded-lg focus:outline-none focus:ring-2`}
        />

        <button
          type="button"
          onClick={() => setShowPassword(!showPassword)}
          className="absolute inset-y-0 right-0 pr-3"
        >
          {showPassword ? <EyeOff /> : <Eye />}
        </button>
      </div>

      {touched && error && (
        <p className="text-sm text-red-600 flex items-center gap-1">
          <AlertCircle className="h-4 w-4" />
          {error}
        </p>
      )}
    </div>
  );
};

export default PasswordField;
